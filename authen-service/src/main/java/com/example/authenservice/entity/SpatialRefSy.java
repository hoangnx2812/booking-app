package com.example.authenservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "spatial_ref_sys")
public class SpatialRefSy {
    @Id
    @Column(name = "srid", nullable = false)
    Integer id;

    @Size(max = 256)
    @Column(name = "auth_name", length = 256)
    String authName;

    @Column(name = "auth_srid")
    Integer authSrid;

    @Size(max = 2048)
    @Column(name = "srtext", length = 2048)
    String srtext;

    @Size(max = 2048)
    @Column(name = "proj4text", length = 2048)
    String proj4text;

}