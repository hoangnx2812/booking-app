package com.example.postservice.controller;

import com.example.commericalcommon.dto.BaseRequest;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.utils.MessageUtil;
import com.example.postservice.dto.request.GetPostRequest;
import com.example.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostsController {
    PostService postService;
    MessageUtil messageUtil;

    @PostMapping(value = "/by-conditions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<Object> getPostsByConditions(
            @Valid @RequestBody BaseRequest<GetPostRequest> baseRequest) {
        long startTime = System.currentTimeMillis();
        return BaseResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestTime(baseRequest.getClientRequestId())
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(postService.getPostsByConditions(baseRequest.getData()))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }
}
