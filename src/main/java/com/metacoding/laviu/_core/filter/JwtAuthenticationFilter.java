package com.metacoding.laviu._core.filter;

import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 단 한번만 실행 되는 필터. 필터가 1번의 요청에 여러번 실행 될 수 있다
// 커스텀 필터를 그냥 추가하고 싶다면 OncePerRequestFilter 를 상속 받아 만들자
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtUtil.HEADER); // 요청 헤더에서 JWT 토큰 추출

        // 그냥 회원가입 및 로그인 요청시 토큰이 없기 때문에 필터 통과 해줘야 함
        if (jwt == null || !jwt.startsWith(JwtUtil.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = jwt.replace(JwtUtil.TOKEN_PREFIX, "");
            Users user = JwtUtil.verify(jwt);

            // 직접 인증 토큰을 만들어서 Authentication 객체를 생성한다
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, // 이 값이 null 이면 인증 처리 부분에서 빠꾸 먹인다
                    null, // password 가 들어가는 부분. 필수가 아니다. null 처리하는게 좋다. 넣으려면 db 조회 해야한다
                    user.getAuthorities()
            );

            // 직접 Authentication 객체를 SecurityContextHolder 에 넣어준다
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.out.println("JWT 오류 : " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
