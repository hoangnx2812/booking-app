package com.example.authenservice.controller;

import com.example.authenservice.service.UserService;
import com.example.commericalcommon.dto.BaseRequest;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("users")
public class UserController {
    UserService userService;
    MessageUtil messageUtil;

    @PostMapping("/info")
    public BaseResponse<Object> getUserInfo(@RequestBody BaseRequest<Object> request) {
        long startTime = System.currentTimeMillis();
        return BaseResponse.builder()
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(userService.getUserInfo())
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }
}
