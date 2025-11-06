package com.example.authenservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "device_name", length = Integer.MAX_VALUE)
    String deviceName;

    @Column(name = "status", length = Integer.MAX_VALUE)
    String status;

    @Column(name = "mobile_serial_number", length = Integer.MAX_VALUE)
    String mobileSerialNumber;

    @Column(name = "mobile_mac_address", length = Integer.MAX_VALUE)
    String mobileMacAddress;

    @Column(name = "mobile_imei", length = Integer.MAX_VALUE)
    String mobileImei;

    @Column(name = "mobile_root_status", length = Integer.MAX_VALUE)
    String mobileRootStatus;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}