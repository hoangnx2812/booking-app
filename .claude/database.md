# Database Design — Toàn bộ Schema & Phân tích

## PostGIS Extensions (cả 2 service)
```sql
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;
CREATE EXTENSION fuzzystrmatch;
CREATE EXTENSION postgis_tiger_geocoder;
```

---

## authen-service Database

### `user_auth` — Thông tin đăng nhập
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| user_name | varchar NOT NULL UNIQUE | username/phone/email |
| user_info_id | bigint NULL | ⚠️ Redundant — `user_info` đã có FK ngược lại |
| user_info_no | varchar NULL | ⚠️ Redundant — có thể lấy từ `user_info` |
| user_pwd_hash | varchar NULL | SHA-256 hash |
| user_salt | varchar NULL | salt cho hashing |
| device_id | varchar NULL | ⚠️ Redundant — đã có bảng `user_device` quản lý |
| number_of_device | int NULL | ⚠️ Redundant — có thể count từ `user_device` |
| platform_version | varchar NULL | |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp | |
| status | varchar NULL | ⚠️ Nên dùng CHECK constraint: 'A','I','D','B' |
| keycloak_id | varchar NULL | ⚠️ Thiếu INDEX — thường xuyên dùng để lookup |

**Indexes cần thêm:** `keycloak_id`

---

### `devices` — Thiết bị vật lý
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| device_name | varchar NULL | |
| status | varchar NULL | ⚠️ Nên chuẩn hóa CHECK constraint |
| mobile_serial_number | varchar NULL | |
| mobile_mac_address | varchar NULL | |
| mobile_imei | varchar NULL | ⚠️ Thiếu INDEX nếu dùng để identify device |
| mobile_root_status | varchar NULL | root/not-rooted |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp | |

**Indexes cần thêm:** `mobile_imei` (nếu dùng để query)

---

### `device_auth` — Key exchange giữa device và server
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| device_id | bigint NOT NULL FK→devices | |
| user_auth_id | bigint NOT NULL FK→user_auth | |
| user_name | varchar NULL | ⚠️ Redundant — có thể lấy qua user_auth_id |
| public_key | text NULL | client public key |
| status | varchar(1) NULL | |
| secret_key | varchar NULL | |
| service_public_key | text NULL | |
| service_private_key | text NULL | ⚠️ SECURITY: private key lưu DB cần encrypt-at-rest |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp | |

**Lưu ý:** Thiếu UNIQUE constraint trên `(device_id, user_auth_id)`

---

### `user_device` — Mapping user ↔ device (quan hệ nhiều-nhiều)
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| user_auth_id | bigint NOT NULL FK→user_auth | |
| device_id | bigint NOT NULL FK→devices | |
| number_of_device | int NULL | ⚠️ Redundant với `user_auth.number_of_device` |
| priority | int NULL | thứ tự ưu tiên thiết bị |
| primary_device | varchar(1) NULL | ⚠️ Nên dùng BOOLEAN |
| last_accessed_time | timestamp(6) NULL | |
| status | varchar(1) NULL | |
| token | varchar(100) NULL | ⚠️ 100 chars có thể ngắn cho một số token format |
| old_token | varchar(100) NULL | |
| check_old_token | varchar(100) NULL | |
| token_change_date | date NULL | |
| biology_method | varchar(2) NULL | biometric: fingerprint/face |
| token_status | smallint NULL | |
| token_user_pwd | varchar(255) NULL | |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp | |

**Lưu ý:** Thiếu UNIQUE constraint trên `(user_auth_id, device_id)`

---

### `role` — Vai trò
| Column | Type | Ghi chú |
|--------|------|---------|
| role_id | bigint IDENTITY PK | ⚠️ Tên không nhất quán, các bảng khác dùng `id` |
| description | varchar(255) NULL | |
| name | varchar(255) NULL | ⚠️ Thiếu UNIQUE constraint, thiếu NOT NULL |
| status | varchar(255) NULL | ⚠️ varchar(255) quá lớn cho status |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp | |

---

### `permission` — Quyền
| Column | Type | Ghi chú |
|--------|------|---------|
| name | varchar(255) PK | dùng làm PK — đổi tên sẽ cascade |
| description | varchar(255) NULL | |

---

### `user_roles` — RBAC: User → Role (junction)
| Column | Type |
|--------|------|
| user_auth_id | bigint FK→user_auth |
| role_id | bigint FK→role |
| PK | (user_auth_id, role_id) |

---

### `role_permissions` — RBAC: Role → Permission (junction)
| Column | Type |
|--------|------|
| role_id | bigint FK→role |
| permissions_name | varchar(255) FK→permission(name) |
| PK | (role_id, permissions_name) |

---

### `area` — Đơn vị hành chính (tỉnh/huyện/xã)
| Column | Type | Ghi chú |
|--------|------|---------|
| id | integer PK | ⚠️ integer thay vì bigint — đủ cho VN (~12k xã) |
| area_type | varchar(5) NOT NULL | P=Tỉnh, D=Huyện, W=Xã |
| area_code | varchar(15) NOT NULL UNIQUE | mã chuẩn |
| parent_code | varchar(15) NULL | ⚠️ Thiếu INDEX cho hierarchical queries |
| province | varchar(10) NULL | |
| district | varchar(10) NULL | |
| precinct | varchar(10) NULL | |
| area_name | varchar(200) NULL | |
| full_name | varchar(500) NULL | |
| order_no | integer NULL | |
| status | varchar(2) NOT NULL | |
| map_code | varchar(15) NULL | |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp | |

**Indexes cần thêm:** `parent_code` cho truy vấn phân cấp

---

### `user_info` — Thông tin cá nhân user
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| user_auth_id | bigint NOT NULL FK→user_auth | ⚠️ Thiếu INDEX riêng (FK tạo index tự động tùy DB) |
| area_id | int NULL FK→area | |
| user_no | varchar(45) NULL | mã định danh nội bộ |
| full_name | varchar(255) NULL | có thể generate từ first+middle+last |
| first_name | varchar(255) NULL | |
| middle_name | varchar(255) NULL | |
| last_name | varchar(255) NULL | |
| display_name | varchar(255) NULL | tên hiển thị (khác full_name) |
| birthday | date NULL | |
| email | varchar(255) NULL | ⚠️ Thiếu UNIQUE constraint nếu email là định danh |
| phone | varchar(255) NULL | ⚠️ varchar(255) quá lớn, varchar(20) là đủ |
| avatar | varchar(1000) NULL | URL MinIO |
| gender | varchar(45) NULL | ⚠️ varchar(45) quá lớn, nên CHECK IN ('M','F','O') |
| address_detail | varchar(255) NULL | |
| full_address | varchar(300) NULL | có thể generate từ address_detail + area |
| description | varchar(1000) NULL | |
| created_at | timestamp DEFAULT now() | |
| updated_at | timestamp NULL | |
| status | varchar(45) NULL | |
| enable_notification | boolean DEFAULT true NOT NULL | |
| enable_email_notification | boolean DEFAULT false NOT NULL | |
| enable_sms | boolean DEFAULT false NOT NULL | |
| enable_promotions_notification | boolean DEFAULT true NOT NULL | |
| enable_promotions_email_notification | boolean DEFAULT true NOT NULL | |
| language | varchar DEFAULT 'vi' NOT NULL | |
| latitude | double precision | denormalized từ location |
| longitude | double precision | denormalized từ location |
| location | geography(Point, 4326) | PostGIS |

---

## customer-service Database

### `user_services` — Dịch vụ làm đẹp của artist
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| created_at | timestamp(6) DEFAULT now() NOT NULL | |
| updated_at | timestamp(6) NULL | |
| title | varchar NULL | tên dịch vụ |
| price | numeric DEFAULT 0 | ⚠️ Nên có precision: NUMERIC(15,2) |
| user_id | bigint NOT NULL | ⚠️ Thiếu INDEX |
| store_id | bigint NOT NULL | ⚠️ Thiếu INDEX, thiếu FK (chưa có bảng store) |
| duration_time | varchar NULL | ⚠️ Nên là INTEGER (phút) hoặc INTERVAL |
| description | varchar NULL | |
| deleted_at | timestamp(6) NULL | soft delete |

**Indexes cần thêm:** `(user_id)`, `(store_id)`, `(user_id, store_id)`

---

### `user_services_map` — Mapping dịch vụ ↔ objects
| Column | Type | Ghi chú |
|--------|------|---------|
| id | bigint IDENTITY PK | |
| created_at | timestamp(6) DEFAULT now() NOT NULL | |
| updated_at | timestamp(6) NULL | |
| object_id | bigint NOT NULL | ID của object (post, promotion,...) |
| object_type | varchar NOT NULL | ⚠️ Nên CHECK IN ('POST','PROMOTION','HASHTAG','SERVICE') |
| user_services_type | varchar NOT NULL | ⚠️ Semantics chưa rõ — cần làm rõ |
| user_services_id | bigint NOT NULL FK→user_services | |

**Indexes cần thêm:** `(object_id, object_type)` cho reverse lookup, `(user_services_id)`

---

## Tổng hợp vấn đề cần cải tiến

### Mức độ CAO (nên fix sớm)

| # | Vấn đề | Bảng | Đề xuất |
|---|--------|------|---------|
| 1 | Thiếu INDEX `keycloak_id` | `user_auth` | `CREATE INDEX idx_user_auth_keycloak_id ON user_auth(keycloak_id)` |
| 2 | `service_private_key` lưu plaintext | `device_auth` | Encrypt-at-rest hoặc dùng secrets manager |
| 3 | `primary_device varchar(1)` | `user_device` | Đổi thành `BOOLEAN` |
| 4 | `duration_time varchar` | `user_services` | Đổi thành `INTEGER` (giá trị bằng phút) |
| 5 | `price numeric` thiếu precision | `user_services` | Đổi thành `NUMERIC(15,2)` |
| 6 | Thiếu INDEX `user_id`, `store_id` | `user_services` | Thêm index cho query performance |
| 7 | Thiếu INDEX `(object_id, object_type)` | `user_services_map` | Cần cho reverse lookup |

### Mức độ TRUNG BÌNH (cải thiện khi có thời gian)

| # | Vấn đề | Bảng | Đề xuất |
|---|--------|------|---------|
| 8 | Redundant `user_info_id`, `user_info_no` | `user_auth` | Xóa — `user_info` đã có FK ngược lại |
| 9 | Redundant `device_id`, `number_of_device` | `user_auth` | Xóa — đã có bảng `user_device` |
| 10 | Redundant `user_name` | `device_auth` | Xóa — lấy qua `user_auth_id` |
| 11 | Redundant `number_of_device` | `user_device` | Xóa — count từ bảng |
| 12 | `role_id` thay vì `id` | `role` | Inconsistent naming |
| 13 | `name` làm PK của `permission` | `permission` | Xem xét dùng surrogate key `id` thay |
| 14 | `role.name` thiếu NOT NULL + UNIQUE | `role` | Thêm constraint |
| 15 | Thiếu UNIQUE `(user_auth_id, device_id)` | `user_device` | Tránh duplicate mapping |
| 16 | Thiếu UNIQUE `(device_id, user_auth_id)` | `device_auth` | Tránh duplicate |
| 17 | Thiếu INDEX `parent_code` | `area` | Cho query cây phân cấp |

### Mức độ THẤP (nice to have)

| # | Vấn đề | Bảng | Đề xuất |
|---|--------|------|---------|
| 18 | `status` không đồng nhất kích thước | nhiều bảng | Chuẩn hóa về `varchar(2)` hoặc `varchar(1)` + CHECK |
| 19 | `full_name`, `full_address` denormalized | `user_info` | OK nếu dùng làm search/display cache |
| 20 | 5 cột boolean notification | `user_info` | Có thể gộp thành JSONB `notification_prefs` (optional) |
| 21 | `gender varchar(45)` quá lớn | `user_info` | Đổi thành `varchar(1)` + CHECK IN ('M','F','O') |
| 22 | `object_type` không có constraint | `user_services_map` | CHECK IN ('POST','PROMOTION','HASHTAG','SERVICE') |
| 23 | `user_services_type` chưa rõ semantics | `user_services_map` | Cần document rõ giá trị hợp lệ |

---

## Các bảng sẽ thêm trong tương lai (customer-service)

Theo roadmap trong `services.md`, customer-service sẽ cần:
- `stores` — thông tin cửa hàng của artist (hiện `store_id` đang orphan)
- `bookings` — lịch hẹn
- `booking_items` — chi tiết từng dịch vụ trong lịch hẹn
- `vouchers` — voucher theo sản phẩm/thời gian
- `voucher_usage` — lịch sử dùng voucher
- `reviews` — đánh giá dịch vụ
- `artist_profiles` — thông tin mở rộng của artist

---

## Ghi chú khi viết code

- `duration_time` hiện là `varchar` → khi đọc/ghi cần parse thành số phút thủ công. Nên convert về `INTEGER` ở migration tiếp theo.
- `store_id` trong `user_services` chưa có FK → không JOIN được, cần xử lý ở application layer.
- `object_type` / `user_services_type` trong `user_services_map` → map với enum `ObjectType` trong `commerical-common`.
- PostGIS `location` field trong `user_info` dùng SRID 4326 (WGS84 — chuẩn GPS).
