# CLAUDE.md — Booking App (Beauty Social Network)

## Tổng quan dự án
Ứng dụng mạng xã hội làm đẹp kết hợp đặt lịch. User có thể lướt bài viết (như Instagram), đặt lịch làm đẹp từ bài viết hoặc trang cá nhân artist. Có 3 vai trò: User thường, Artist (đăng ký + admin duyệt), Admin.

## Chi tiết → xem các file con
- [Kiến trúc & Tech Stack](./.claude/architecture.md)
- [Conventions & Patterns](./.claude/conventions.md)
- [Từng Service chi tiết](./.claude/services.md)
- [Workflow & Deploy](./.claude/workflow.md)

---

## Quy tắc bắt buộc khi viết code

### 1. Request/Response luôn bọc bởi wrapper
- **API thường**: `BaseRequest<T>` / `BaseResponse<T>`
- **List/Page**: `BaseResponse<PageResponse<T>>`
- Class nằm ở: `commerical-common/src/main/java/com/example/commericalcommon/dto/`
- **Không bao giờ** trả về raw object hoặc tự tạo wrapper mới

### 2. Exception handling
- Throw `GlobalException(ErrorCode)` cho mọi lỗi nghiệp vụ
- `ErrorCode` enum nằm ở `commerical-common` — thêm code mới vào đây (Option A)
- Nếu service cần xử lý lỗi riêng biệt thì tạo exception riêng, nhưng ưu tiên dùng `GlobalException`
- Format lỗi tự động trả về qua `GlobalExceptionHandler` (@RestControllerAdvice trong common)

### 3. Database: JPA + JDBC cả hai
- **JPA**: CRUD đơn giản, entity relationships
- **JDBC (custom Repository)**: Query phức tạp, pagination tùy chỉnh, batch insert
- Đặt tên: `{Entity}Repository` (JPA), `{Entity}RepositoryJdbc` (JDBC)

### 4. Test
- Viết test khi: (a) được yêu cầu, hoặc (b) khi tạo API mới
- Framework: JUnit 5 + Mockito
- Đặt test trong `src/test/java/` cùng package với class được test

### 5. Code style
- Làm đúng những gì được yêu cầu
- Có thể thêm những gì hợp lý (sẽ được review)
- Không over-engineer, không thêm feature không được yêu cầu
- Đọc code hiện tại của service liên quan trước khi viết

### 6. HTTP Client (inter-service)
- Dùng **OpenFeign** (blocking) cho hầu hết inter-service call
- Có thể dùng **WebClient** (reactive) nếu endpoint xử lý nhiều concurrent request
- Không mix WebFlux dispatcher với WebMVC trong cùng 1 app
- Internal API prefix: `/internal/**` (không đi qua api-gateway auth filter)
