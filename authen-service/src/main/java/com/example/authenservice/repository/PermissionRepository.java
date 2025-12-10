package com.example.authenservice.repository;

import com.example.authenservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    boolean existsByName(String name);
}
