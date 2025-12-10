package com.example.authenservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "device_auth")
public class DeviceAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    Device device;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_auth_id", nullable = false)
    UserAuth userAuth;

    @Column(name = "user_name", length = Integer.MAX_VALUE)
    String userName;

    @Column(name = "public_key", length = Integer.MAX_VALUE)
    String publicKey;

    @Size(max = 1)
    @Column(name = "status", length = 1)
    String status;

    @Column(name = "secret_key", length = Integer.MAX_VALUE)
    String secretKey;

    @Column(name = "service_public_key", length = Integer.MAX_VALUE)
    String servicePublicKey;

    @Column(name = "service_private_key", length = Integer.MAX_VALUE)
    String servicePrivateKey;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}