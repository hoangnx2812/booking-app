package com.example.authenservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @jakarta.validation.constraints.Size(max = 255)
    @Column(name = "name", nullable = false)
    String name;

    @jakarta.validation.constraints.Size(max = 255)
    @Column(name = "description")
    String description;

}