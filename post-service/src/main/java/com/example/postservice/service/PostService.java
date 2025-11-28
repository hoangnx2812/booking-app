package com.example.postservice.service;

import java.util.List;

public interface PostService {

    List<Object> getPostsByConditions(Object post);

    Object addPost(Object post);

    Object deletePost(Object post);

    Object updatePost(Object post);
}
