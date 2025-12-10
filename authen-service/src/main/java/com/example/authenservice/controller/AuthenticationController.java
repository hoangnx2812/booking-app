package com.example.authenservice.controller;

import com.example.authenservice.dto.request.LoginUserRequest;
import com.example.authenservice.dto.request.RegisterUserRequest;
import com.example.authenservice.service.AuthenticationService;
import com.example.commericalcommon.dto.BaseRequest;
import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.dto.request.IdRequest;
import com.example.commericalcommon.dto.request.IntrospectRequest;
import com.example.commericalcommon.dto.response.IntrospectResponse;
import com.example.commericalcommon.utils.MessageUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    MessageUtil messageUtil;

    @PostMapping("/login")
    public BaseResponse<Object> login(@RequestBody BaseRequest<LoginUserRequest> request) {
        long startTime = System.currentTimeMillis();
        return BaseResponse.builder()
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("FETCH_SUCCESS"))
                .data(authenticationService.loginUser(request.getData()))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }

    @PostMapping("/register")
    public BaseResponse<Object> register(@RequestBody BaseRequest<RegisterUserRequest> request) {
        long startTime = System.currentTimeMillis();
        authenticationService.registerUser(request.getData());
        return BaseResponse.builder()
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("REGISTRATION_SUCCESS"))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }

    @DeleteMapping("/delete")
    public BaseResponse<Object> delete(@RequestBody BaseRequest<IdRequest> request) {
        long startTime = System.currentTimeMillis();
        authenticationService.deleteUser(request.getData());
        return BaseResponse.builder()
                .requestId(request.getRequestId())
                .requestTime(request.getClientRequestId())
                .resultDesc(messageUtil.getMessage("DELETE_SUCCESS"))
                .cost(System.currentTimeMillis() - startTime)
                .build();
    }

    @PostMapping("/introspect")
    public Mono<BaseResponse<IntrospectResponse>> introspect(@Valid @RequestBody BaseRequest<IntrospectRequest> request) {
        long startTime = System.currentTimeMillis();
        return authenticationService.introspectToken(request.getData())
                .map(resp -> BaseResponse.<IntrospectResponse>builder()
                        .requestId(request.getRequestId())
                        .requestTime(request.getClientRequestId())
                        .resultDesc("FETCH_SUCCESS")
                        .data(resp)
                        .cost(System.currentTimeMillis() - startTime)
                        .build()
                );
    }
}