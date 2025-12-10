package com.example.authenservice.repository;

import com.example.authenservice.entity.RolePermission;
import com.example.authenservice.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission,Long> {
    boolean existsById(RolePermissionId id);
}
