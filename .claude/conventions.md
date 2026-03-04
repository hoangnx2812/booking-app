# Conventions & Patterns

## Package Structure
```
com.example.{service-name}/
  ├── config/          ← Security, JPA, Feign, Minio, Redis config
  ├── controller/      ← REST controllers
  ├── service/         ← Interface + Impl
  ├── repository/      ← JpaRepository + custom JDBC repos
  ├── entity/          ← JPA entities
  ├── dto/             ← Request/Response DTOs (local, không phải common)
  ├── client/          ← Feign clients (inter-service)
  └── enums/           ← Service-specific enums
```

## Request/Response Pattern

### Tất cả API phải bọc bởi BaseRequest/BaseResponse
```java
// Controller nhận vào
@PostMapping("/example")
public BaseResponse<SomeResponse> example(@RequestBody BaseRequest<SomeRequest> request) {
    SomeRequest data = request.getData();
    // ...
    return BaseResponse.<SomeResponse>builder()
        .resultCode(ErrorCode.SUCCESS.getCode())
        .resultDesc(ErrorCode.SUCCESS.getMessage())
        .data(result)
        .build();
}

// Trả về page/list
public BaseResponse<PageResponse<SomeDTO>> getList(...) {
    // ...
    PageResponse<SomeDTO> page = PageResponse.<SomeDTO>builder()
        .content(items)
        .totalElements(total)
        .totalPages(totalPages)
        .currentPage(currentPage)
        .pageSize(pageSize)
        .build();
    return BaseResponse.<PageResponse<SomeDTO>>builder()
        .resultCode(ErrorCode.SUCCESS.getCode())
        .data(page)
        .build();
}
```

### BaseRequest fields quan trọng
```
requestId, clientRequestId    ← tracking
userId, userInfoId            ← authenticated user
deviceId, deviceName          ← device info
channelCode                   ← kênh gọi
longitude, latitude           ← geo location
data: T                       ← payload thực sự
```

## Exception Handling

### Throw GlobalException
```java
// Trong service
throw new GlobalException(ErrorCode.USER_NOT_EXISTED);
throw new GlobalException(ErrorCode.INVALID_INPUT, "Custom message here");
```

### Thêm ErrorCode mới
- File: `commerical-common/.../enums/ErrorCode.java`
- Format: `CODE_NAME("XX", "message", HttpStatus.XXX)`
- Các code hiện có:
  - "00" SUCCESS
  - "01" UNCATEGORIZED (500)
  - "02" METHOD_NOT_ALLOWED (405)
  - "03" UNAUTHORIZED (403)
  - "04" UNAUTHENTICATED (401)
  - "05" INVALID_INPUT (400)
  - "06" INVALID_ENDPOINT (404)
  - "07" RESOURCE_NOT_FOUND (404)
  - "08" USER_NOT_EXISTED (404)
  - "09" ROLE_NOT_EXISTED (404)
  - "10" SESSION_EXPIRED (401)
  - "11" POST_NOT_FOUND (404)

## Database Conventions

### Entity
```java
@Entity
@Table(name = "table_name")  // snake_case
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "column_name")   // snake_case
    private String fieldName;       // camelCase

    private String status;           // dùng Status enum: A/I/D/B
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt; // soft delete
}
```

### Soft Delete Pattern
- Không xóa vật lý, set `deleted_at = now()`
- Status: `ACTIVE(A)`, `INACTIVE(I)`, `DELETED(D)`, `BLOCKED(B)`

### Repository Naming
- `{Entity}Repository` → extends `JpaRepository` (CRUD đơn giản)
- `{Entity}RepositoryJdbc` → custom JDBC (query phức tạp, pagination, batch)

### Khi nào dùng JDBC thay JPA
- Query có nhiều join + điều kiện động
- Pagination tùy chỉnh phức tạp
- Batch insert/update (performance)

## Naming Conventions

| Type | Convention | Example |
|------|-----------|---------|
| Class | PascalCase | `PostService`, `UserInfoDTO` |
| Method | camelCase | `getUserById()` |
| Variable | camelCase | `userInfoId` |
| Constant | UPPER_SNAKE | `SUCCESS_CODE` |
| Table | snake_case | `user_info` |
| Column | snake_case | `user_info_id` |
| Package | lowercase | `com.example.postservice` |
| DTO suffix | Request/Response | `LoginRequest`, `UserInfoResponse` |
| Service | Interface + Impl | `PostService` + `PostServiceImpl` |
| Feign | `{Target}Client` | `AuthenticationClient` |

## Security & Auth

### Lấy user hiện tại từ JWT
```java
// Trong controller/service
@PreAuthorize("hasRole('USER')")
public ResponseEntity<?> method(Authentication authentication) {
    String username = authentication.getName(); // preferred_username
    // Roles đến từ realm_access.roles + resource_access.bka_app.roles
}
```

### Roles
- `ROLE_USER`, `ROLE_ARTIST`, `ROLE_ADMIN`
- Prefix `ROLE_` tự động thêm bởi JwtAuthConverter

## Utility Functions (commerical-common)

```java
// Tạo unique ID
Util.generateNo("PREFIX")  // PREFIX + timestamp + random

// Tạo tên file unique
Util.generateObjectName(originalName, mimeType)

// Hash SHA-256
Util.encryptSHA256(input)

// Salt generation
Util.generateSalt(numBytes)
```

## Redis Pattern
```java
// Inject RedisService (interface từ commerical-common)
redisService.hset(key, field, value, ttlSeconds);
redisService.hget(key, field, MyDTO.class);
redisService.hdel(key, field);
```

## Logging Pattern
```
Format: "%d{yyyy-MM-dd HH:mm:ss.SSS} | %-5level | %X{traceId:-N/A} | %thread | %C{1}.%M:%L | %msg%n"
File: logs/{service-name}.log
```
Luôn dùng `@Slf4j` (Lombok), không dùng `System.out.println`.

## MinIO File Upload
- Bucket: `attachment`
- Dùng `AttachmentFolder` enum để xác định folder
- Tên file: `Util.generateObjectName(name, mimeType)`
- Upload qua `MinioService` (trong từng service)

## Internal API Pattern
- Prefix: `/internal/**`
- Không qua api-gateway auth filter
- Chỉ được gọi từ các service nội bộ
- Không expose ra ngoài
