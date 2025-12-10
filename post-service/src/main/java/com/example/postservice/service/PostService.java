package com.example.postservice.service;

import com.example.commericalcommon.dto.PageResponse;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.dto.response.GetPostsResponse;

public interface PostService {

    PageResponse<GetPostsResponse> getPostsByConditions(GetPostRequest request);

    Object addPost(Object post);

    Object deletePost(Object post);

    Object updatePost(Object post);
}
