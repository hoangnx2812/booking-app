package com.example.postservice.service;

public interface PostBlockRepository {
    Object addPostBlock(Object postBlock);

    Object deletePostBlock(Object postBlock);

    Object getPostBlockByConditions(Object postBlock);
}
