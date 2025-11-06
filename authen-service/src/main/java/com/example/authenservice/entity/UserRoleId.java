package com.example.authenservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
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
public class UserRoleId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8938997995924383976L;

    @NotNull
    @Column(name = "user_auth_id", nullable = false)
    Long userAuthId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    Long roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRoleId entity = (UserRoleId) o;
        return Objects.equals(this.userAuthId, entity.userAuthId) &&
                Objects.equals(this.roleId, entity.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAuthId, roleId);
    }

}