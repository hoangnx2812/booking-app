package com.example.authenservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Embeddable
public class RolePermissionId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7509283248738682471L;

    @NotNull
    @Column(name = "role_id", nullable = false)
    Long roleId;

    @Size(max = 255)
    @NotNull
    @Column(name = "permissions_name", nullable = false)
    String permissionsName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RolePermissionId entity = (RolePermissionId) o;
        return Objects.equals(this.roleId, entity.roleId) &&
                Objects.equals(this.permissionsName, entity.permissionsName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionsName);
    }

}