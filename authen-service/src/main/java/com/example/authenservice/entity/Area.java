package com.example.authenservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "area")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 5)
    @NotNull
    @Column(name = "area_type", nullable = false, length = 5)
    private String areaType;

    @Size(max = 15)
    @NotNull
    @Column(name = "area_code", nullable = false, length = 15)
    private String areaCode;

    @Size(max = 15)
    @Column(name = "parent_code", length = 15)
    private String parentCode;

    @Size(max = 10)
    @Column(name = "province", length = 10)
    private String province;

    @Size(max = 10)
    @Column(name = "district", length = 10)
    private String district;

    @Size(max = 10)
    @Column(name = "precinct", length = 10)
    private String precinct;

    @Size(max = 200)
    @Column(name = "area_name", length = 200)
    private String areaName;

    @Size(max = 500)
    @Column(name = "full_name", length = 500)
    private String fullName;

    @Column(name = "order_no")
    private Integer orderNo;

    @Size(max = 2)
    @NotNull
    @Column(name = "status", nullable = false, length = 2)
    private String status;

    @Size(max = 15)
    @Column(name = "map_code", length = 15)
    private String mapCode;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "area")
    private Set<UserInfo> userInfos = new LinkedHashSet<>();

}