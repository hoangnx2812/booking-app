package com.example.postservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post_medias")
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "media_id")
    private Long mediaId;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "post_medias_type", length = Integer.MAX_VALUE)
    private String postMediasType;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}