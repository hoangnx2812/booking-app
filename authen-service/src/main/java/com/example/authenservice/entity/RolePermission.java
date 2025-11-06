package com.example.authenservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "role_permissions")
public class RolePermission {
    @EmbeddedId
    private RolePermissionId id;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @MapsId("permissionsName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permissions_name", nullable = false)
    Permission permissionsName;

}