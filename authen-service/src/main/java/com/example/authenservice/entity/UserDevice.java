package com.example.authenservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "user_device")
public class UserDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_auth_id", nullable = false)
    UserAuth userAuth;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    Device device;

    @Column(name = "number_of_device")
    Integer numberOfDevice;

    @Column(name = "priority")
    Integer priority;

    @Size(max = 1)
    @Column(name = "primary_device", length = 1)
    String primaryDevice;

    @Column(name = "last_accessed_time")
    Instant lastAccessedTime;

    @Size(max = 1)
    @Column(name = "status", length = 1)
    String status;

    @Size(max = 100)
    @Column(name = "token", length = 100)
    String token;

    @Size(max = 100)
    @Column(name = "old_token", length = 100)
    String oldToken;

    @Size(max = 100)
    @Column(name = "check_old_token", length = 100)
    String checkOldToken;

    @Column(name = "token_change_date")
    LocalDate tokenChangeDate;

    @Size(max = 2)
    @Column(name = "biology_method", length = 2)
    String biologyMethod;

    @Column(name = "token_status")
    Short tokenStatus;

    @Size(max = 255)
    @Column(name = "token_user_pwd")
    String tokenUserPwd;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}