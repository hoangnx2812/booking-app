package com.example.postservice.configuration;


import com.example.commericalcommon.dto.BaseResponse;
import com.example.commericalcommon.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        BaseResponse<?> baseResponse = BaseResponse.builder()
                .resultCode(errorCode.getCode())
                .resultDesc(errorCode.getMessage())
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(baseResponse));
        response.flushBuffer();
    }
}
