package com.example.postservice.service;

public interface PostCommentService {
    Object addPostComment(Object post);

    Object deletePostComment(Object post);

    Object updatePostComment(Object post);

    Object getPostCommentByConditions(Object post);
}
