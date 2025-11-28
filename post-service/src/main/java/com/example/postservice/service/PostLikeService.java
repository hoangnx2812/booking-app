package com.example.postservice.service;

public interface PostLikeService {
    Object addPostLike(Object postLike);

    Object deletePostLike(Object postLike);

    Object getPostLikeByConditions(Object postLike);
}
