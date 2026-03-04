# Workflow & Deploy

## Git Workflow

### Branch Strategy
| Branch | Môi trường | Mô tả |
|--------|-----------|-------|
| `dev` | Development | Code đang phát triển |
| `main` | UAT | Code đã ổn định |

### Commit Convention
```
vX.X.X: mô tả ngắn thay đổi

Ví dụ:
v1.0.15: add post like API
v1.0.16: fix user register bug
v1.0.17: add booking service draft
```

### Solo workflow
- Không có PR review từ người khác
- SonarQube tích hợp IDE để check code quality
- Tự review trước khi merge dev → main

---

## CI/CD

### Stack
- **Jenkins** chạy trên máy Ubuntu (máy dev nhà)
- Deploy trực tiếp trên Ubuntu server

### Môi trường
- `dev` branch → deploy lên dev environment
- `main` branch → deploy lên UAT environment

---

## Khởi chạy local

### Prerequisites
- Docker Desktop (chạy docker-compose)
- Java 21
- Maven

### Start infrastructure
```bash
docker-compose up -d
# Starts: Keycloak, PostgreSQL, Redis, MinIO, Kafka
```

### Start services (thứ tự)
1. `authen-service` (port 8080) — khởi động trước vì các service khác phụ thuộc
2. `customer-service` (port 8082)
3. `post-service` (port 8081)
4. `api-gateway` (port 8888) — khởi động sau cùng

### Verify
- Keycloak Admin: http://localhost:8090 (admin/admin123)
- MinIO Console: http://localhost:9001 (bkaadmin/bkaadmin)
- API Docs: http://localhost:{port}/swagger-ui.html

---

## Thêm Feature mới (Checklist)

### Thêm API mới vào service hiện có
- [ ] Đọc code hiện tại của service để hiểu pattern
- [ ] Tạo Request/Response DTO
- [ ] Thêm ErrorCode mới vào commerical-common nếu cần
- [ ] Viết Entity (nếu cần bảng mới)
- [ ] Viết Repository (JPA và/hoặc JDBC tùy độ phức tạp)
- [ ] Viết Service Interface + Impl
- [ ] Viết Controller với `BaseRequest`/`BaseResponse`
- [ ] Viết Unit Test (JUnit 5 + Mockito)
- [ ] Thêm SQL migration script nếu thêm bảng mới

### Thêm service mới
- [ ] Tạo Spring Boot module mới
- [ ] Add dependency `commerical-common` vào pom.xml
- [ ] Copy SecurityConfig, JwtAuthConverter từ service khác
- [ ] Cấu hình routes trong `api-gateway/application.yml`
- [ ] Thêm database mới (database per service)
- [ ] Tạo Feign client ở các service cần gọi vào

---

## Database Migration
- Hiện tại: script SQL thủ công (`*-script.sql` ở root)
- Tương lai: cân nhắc Flyway hoặc Liquibase

---

## Kafka (Booking Service - Tương lai)
- Kafka đã ready trong docker-compose (KRaft mode, port 9094)
- Booking service sẽ là **project/repo riêng**
- customer-service sẽ publish event, booking-service consume
