package com.example.postservice.repository;

import com.example.postservice.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Integer countByPosts_Id(Long posts_id);
}
