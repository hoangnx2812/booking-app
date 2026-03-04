# Chi tiết từng Service

## 1. commerical-common (Shared Library)

**Path:** `commerical-common/`
**Role:** Lib dùng chung, không phải deployable service

### Nội dung chính
```
dto/
  BaseRequest<T>      ← wrapper cho mọi request
  BaseResponse<T>     ← wrapper cho mọi response
  PageResponse<T>     ← wrapper cho list/page response

enums/
  ErrorCode           ← tất cả error codes (thêm mới vào đây)
  Status              ← ACTIVE(A), INACTIVE(I), DELETED(D), BLOCKED(B)
  ObjectType          ← POST, PROMOTION, HASHTAG, SERVICE
  MimeType            ← MIME type ↔ extension mapping
  AttachmentFolder    ← folder structure cho MinIO

exception/
  GlobalException     ← extends RuntimeException, có errorCode + customMessage
  GlobalExceptionHandler ← @RestControllerAdvice, xử lý toàn bộ exception

service/
  RedisService (interface)  ← hset/hget/hdel với TTL

utils/
  Util                ← generateNo, generateSalt, encryptSHA256, generateObjectName
  Constant            ← SUCCESS_CODE, PUBLIC_ENDPOINTS, DEFAULT_ROLES, PrefixNo
  MessageUtil         ← i18n message resolution

config/
  JacksonConfig       ← ObjectMapper bean
```

### Quan trọng
- Khi thêm ErrorCode mới → sửa file này
- Mọi service đều import lib này qua pom.xml
- GlobalExceptionHandler được scan bởi tất cả service (`@RestControllerAdvice`)

---

## 2. api-gateway (Port 8888)

**Path:** `api-gateway/`
**Role:** Entry point duy nhất, routing + auth validation cho FE

### Routes
| Path | Target |
|------|--------|
| `/authentication/**` | `http://localhost:8080` |
| `/post/**` | `http://localhost:8081` |
| `/customer/**` | `http://localhost:8082` |

### Auth Filter
- Public: `/authentication/auth/.*` → forward thẳng (không check token)
- Còn lại: kiểm tra `Authorization: Bearer <token>`
- Không có token → 401
- Token hợp lệ → forward request

### Khi thêm service mới
1. Thêm route vào `application.yml`
2. Thêm public endpoints nếu cần vào filter

---

## 3. authen-service (Port 8080)

**Path:** `authen-service/`
**Role:** Quản lý authentication, user profile, tích hợp Keycloak

### API Endpoints
```
POST /authentication/auth/login       ← đăng nhập (public)
POST /authentication/auth/register    ← đăng ký (public)
POST /authentication/auth/introspect  ← validate token (public)
DELETE /authentication/auth/delete    ← xóa user

GET/POST /authentication/users/**     ← quản lý user profile

POST /authentication/internal/users/by-conditions  ← internal, dùng bởi post-service
```

### Entities
- `UserAuth`: credentials (username, passwordHash, salt, keycloakId)
- `UserInfo`: profile (name, email, phone, avatar, location/PostGIS)
- `Role`, `Permission`, `UserRole`, `RolePermission`: RBAC
- `Device`, `DeviceAuth`, `UserDevice`: device management
- `Area`: khu vực địa lý

### Keycloak Integration
- Realm: `bka`, Client: `bka_app`
- Register → tạo user trong DB + sync lên Keycloak
- Login → lấy OAuth2 token từ Keycloak
- 2 Keycloak beans: `admin` (PASSWORD grant) + `app` (CLIENT_CREDENTIALS)
- Token introspection qua WebClient (reactive)

### Đặc biệt
- Dùng WebFlux (Mono/Flux) cho gọi Keycloak
- SHA-256 + salt cho password hash (trước khi sync Keycloak)
- PostGIS cho location data của user

---

## 4. customer-service (Port 8082)

**Path:** `customer-service/`
**Role:** Service lớn nhất, chứa nhiều logic nghiệp vụ nhất (đang phát triển)

### Phạm vi (sẽ mở rộng thêm)
- Quản lý dịch vụ làm đẹp của user/artist
- Đặt lịch, theo dõi lịch hẹn
- Voucher (theo sản phẩm, theo thời gian)
- Đánh giá dịch vụ
- Quản lý thông tin cá nhân artist

### Entities hiện có
- `UserService`: dịch vụ (title, price, durationTime, description, userId, storeId)
- `UserServicesMap`: mapping service ↔ objects (POST, PROMOTION, etc.)

### API Endpoints
```
POST /customer/internal/get-user-services  ← internal, dùng bởi post-service
```

### Repositories
- `UserServiceRepository` (JPA)
- `UserServiceMapRepository` (JPA)
- `UserServiceJdbcRepository` (JDBC custom)

---

## 5. post-service (Port 8081)

**Path:** `post-service/`
**Role:** Quản lý bài viết, media, hashtag, interactions

### API Endpoints
```
POST /post/posts/by-conditions  ← lấy danh sách bài viết (public)
POST /post/posts/insert         ← tạo bài viết mới
POST /post/likes/**             ← like/unlike
POST /post/comments/**          ← comment management
```

### Entities
- `Post`: bài viết (title, userInfoId, storeId, servicesIds, priority, quảng cáo fields)
- `PostLike`, `PostComment`, `PostBlock`: interactions
- `PostView`, `PostReport`: analytics & moderation
- `PostStatusLog`: audit log status changes
- `Plans`: gói advertising
- `Attachment`: file media (fileName, thumbnailUrl, filePaths, mimeType, size, checksum)
- `AttachmentMap`: mapping file ↔ object (POST, PROMOTION, etc.)
- `Hashtag`, `HashtagMap`: tag system

### Feign Clients
```java
// Gọi authen-service
AuthenticationClient.getUsersByConditions(GetUserInfoRequest)
→ POST http://localhost:8080/authentication/internal/users/by-conditions

// Gọi customer-service
CustomerClient.getUserServices(GetUserServiceRequest)
→ POST http://localhost:8082/customer/internal/get-user-services
```

### Repositories
- Standard JPA repos cho CRUD
- `PostsRepositoryJdbc`: pagination phức tạp cho feed
- `AttachmentRepositoryJdbc`: complex attachment queries
- `HashtagRepositoryJdbc`: hashtag search & statistics

---

## Service sẽ phát triển trong tương lai

### booking-kafka-service (Project riêng)
- Xử lý đặt lịch qua Kafka
- Tách riêng để không blocking customer-service
- Kafka đã có trong docker-compose (KRaft mode, port 9094)

### notification-service
- Thông báo đặt lịch, nhắc lịch, like/comment

### chat-service
- Chat giữa user và artist
