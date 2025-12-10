package com.example.postservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "plans")
public class Plans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "plans_type", length = Integer.MAX_VALUE)
    private String plansType;

    @Column(name = "plans_name", length = Integer.MAX_VALUE)
    private String plansName;

    @ColumnDefault("0")
    @Column(name = "price_vi", precision = 10, scale = 2)
    private BigDecimal priceVi;

    @ColumnDefault("0")
    @Column(name = "price_en", precision = 10, scale = 2)
    private BigDecimal priceEn;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "unit", length = Integer.MAX_VALUE)
    private String unit;

    @Column(name = "standard_listing")
    private Boolean standardListing;

    @ColumnDefault("false")
    @Column(name = "allow_kar")
    private Boolean allowKar;

    @Column(name = "price_kar")
    private Integer priceKar;

    @Column(name = "manual_renew")
    private Boolean manualRenew;

    @Column(name = "auto_renew")
    private Boolean autoRenew;

    @Column(name = "show_on_latest_listing")
    private Boolean showOnLatestListing;

    @Column(name = "show_on_main_listing")
    private Boolean showOnMainListing;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}