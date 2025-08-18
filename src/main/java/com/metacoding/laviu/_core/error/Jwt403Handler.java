package com.metacoding.laviu._core.error;

import com.metacoding.laviu._core.utils.RespFilterUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class Jwt403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 컨텐츠 타입을 application/json 으로
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 상태코드를 상수값으로 403
        PrintWriter out = response.getWriter(); // 응답 버퍼에 작성
        String responseBody = RespFilterUtil.fail(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        out.println(responseBody);
        out.flush();
    }
}