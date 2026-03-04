# Kiến trúc & Tech Stack

## Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.5.x (mỗi service có thể khác nhau) |
| Cloud | Spring Cloud | 2025.x |
| Auth | Keycloak (OAuth2 + JWT) | 26.0.2 |
| Database | PostgreSQL + PostGIS | 15 |
| Cache | Redis (Jedis client) | 7.0 |
| File Storage | MinIO (S3-compatible) | latest |
| Messaging | Apache Kafka (KRaft mode) | latest |
| Inter-service | OpenFeign / WebClient | Spring Cloud |
| ORM | Spring Data JPA + JDBC Template | - |
| Security | Spring Security OAuth2 Resource Server | - |
| API Docs | SpringDoc OpenAPI (Swagger) | 2.8.9 |

## Service Map

```
[Frontend / External]
        │
        ▼
  [api-gateway :8888]   ← entry point duy nhất cho FE
        │
        ├──► [authen-service :8080]   ← auth, user profile
        ├──► [post-service :8081]     ← bài viết, like, comment
        └──► [customer-service :8082] ← dịch vụ, đặt lịch, voucher

[Services nội bộ gọi nhau qua internal API, không qua gateway]
  post-service ──Feign──► authen-service (/internal/users/by-conditions)
  post-service ──Feign──► customer-service (/internal/get-user-services)

[Infrastructure]
  PostgreSQL (per-service DB) + PostGIS
  Redis (cache chung)
  MinIO (file storage)
  Kafka (KRaft, dùng cho booking service tương lai - project riêng)
```

## Security Flow

```
Request → api-gateway
  → Kiểm tra public endpoint? (/authentication/auth/.*)
    → Yes: forward thẳng
    → No: validate Bearer token
      → Invalid: 401
      → Valid: forward đến service + pass userId trong header

Mỗi service:
  → Spring Security OAuth2 Resource Server
  → JWT từ Keycloak (issuer: http://localhost:8090/realms/bka)
  → JwtAuthConverter: extract roles từ realm_access + resource_access
  → Principal: preferred_username
  → Session: STATELESS
```

## Public Endpoints (không cần token)
- `/auth/register`, `/auth/login`
- `/public/**`
- `/posts/by-conditions`
- `/internal/**`

## Infrastructure (Docker)

| Service | Port | Credentials |
|---------|------|-------------|
| Keycloak | 8090 | admin / admin123 |
| PostgreSQL (Keycloak) | 5432 | - |
| PostgreSQL (App) | 5432 (PostGIS) | root / root |
| Redis | 6379 | - |
| MinIO | 9000-9001 | bkaadmin / bkaadmin |
| Kafka | 9094 | - |

**Keycloak config:**
- Realm: `bka`
- Client ID: `bka_app`

## Database per Service
- `authen-service`: `user_auth`, `user_info`, `role`, `permission`, `device`, `area`
- `customer-service`: `user_services`, `user_services_map`
- `post-service`: `posts`, `post_likes`, `post_comments`, `post_reports`, `attachment`, `hashtags`, `plans`

## Feign Client Config (application.yml)
```yaml
app:
  services:
    authentication:
      url: http://localhost:8080/authentication
    customer:
      url: http://localhost:8082/customer
```
