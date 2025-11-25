package com.example.postservice.repository;

import com.example.postservice.entity.PostBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostBlockRepository extends JpaRepository<PostBlock, Long> {
}
