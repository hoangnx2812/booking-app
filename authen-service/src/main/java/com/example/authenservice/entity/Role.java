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
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    Long id;

    @jakarta.validation.constraints.Size(max = 255)
    @Column(name = "description")
    String description;

    @jakarta.validation.constraints.Size(max = 255)
    @Column(name = "name")
    String name;

    @jakarta.validation.constraints.Size(max = 255)
    @Column(name = "status")
    String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}