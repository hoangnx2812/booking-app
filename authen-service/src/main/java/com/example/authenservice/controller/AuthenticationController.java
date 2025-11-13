package com.example.authenservice.controller;

import com.example.authenservice.dto.request.LoginUserRequest;
import com.example.authenservice.dto.request.RegisterUserRequest;
import com.example.authenservice.dto.response.LoginUserResponse;
import com.example.authenservice.service.AuthenticationService;
import com.example.commericalcommon.dto.BaseRequest;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.IdRequest;
import com.example.commericalcommon.utils.MessageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    MessageUtil messageUtil;

    @PostMapping("/login")
    public BaseResponse<LoginUserResponse> login(@RequestBody BaseRequest<LoginUserRequest> request) {
        long startTime = System.currentTimeMillis();
        LoginUserResponse response = authenticationService.loginUser(request.getData());
        return BaseResponse.<LoginUserResponse>builder()
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(response)
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }

    @PostMapping("/register")
    public BaseResponse<Void> register(@RequestBody BaseRequest<RegisterUserRequest> request) {
        long startTime = System.currentTimeMillis();
        authenticationService.registerUser(request.getData());
        return BaseResponse.<Void>builder()
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("REGISTRATION_SUCCESS"))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }

    @DeleteMapping("/delete")
    public BaseResponse<Void> delete(@RequestBody BaseRequest<IdRequest> request) {
        long startTime = System.currentTimeMillis();
        authenticationService.deleteUser(request.getData());
        return BaseResponse.<Void>builder()
                .requestId(request.getRequestId())
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("DELETE_SUCCESS"))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }
}