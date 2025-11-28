package com.example.postservice.service.impl;

import com.example.postservice.repository.PostsRepository;
import com.example.postservice.repository.jdbc.PostsRepositoryJdbc;
import com.example.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostsRepositoryJdbc postRepositoryJdbc;
    PostsRepository postsRepository;

    @Override
    public List<Object> getPostsByConditions(Object post) {
        return List.of();
    }

    @Override
    public Object addPost(Object post) {
        return null;
    }

    @Override
    public Object deletePost(Object post) {
        return null;
    }

    @Override
    public Object updatePost(Object post) {
        return null;
    }
}
