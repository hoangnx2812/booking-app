package com.example.postservice.repository;

import com.example.postservice.entity.PostStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStatusLogRepository extends JpaRepository<PostStatusLog, Long> {
}
