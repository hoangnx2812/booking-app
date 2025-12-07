package com.example.postservice.service.impl;

import com.example.commericalcommon.dto.PageResponse;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;
import com.example.postservice.repository.PostsRepository;
import com.example.postservice.repository.jdbc.PostsRepositoryJdbc;
import com.example.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
    PostsRepositoryJdbc postRepositoryJdbc;
    PostsRepository postsRepository;

    @Override
    public PageResponse<GetPostsResponse> getPostsByConditions(GetPostRequest request) {
        int offset = (request.getPage() - 1) * request.getSize();
//        Long totalElements = postRepositoryJdbc.totalElements(request);
//        int totalPages = (int) Math.ceil((double) totalElements / request.getSize());
        return PageResponse.<GetPostsResponse>builder()
                .content(postRepositoryJdbc.getPostsByConditions(request, offset))
//                .totalElements(totalElements)
//                .totalPages(totalPages)
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .build();
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
