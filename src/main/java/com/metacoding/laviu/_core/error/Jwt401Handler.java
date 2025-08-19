package com.metacoding.laviu._core.error;

import com.metacoding.laviu._core.utils.RespFilterUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

public class Jwt401Handler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 컨텐츠 타입을 application/json 으로
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 상태코드를 상수값으로 401
        PrintWriter out = response.getWriter(); // 응답 버퍼에 작성
        String responseBody = RespFilterUtil.fail(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        out.println(responseBody);
        out.flush();
    }
}