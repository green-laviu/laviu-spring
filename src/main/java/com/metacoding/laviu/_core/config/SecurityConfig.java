package com.metacoding.laviu._core.config;

import com.metacoding.laviu._core.error.Jwt401Handler;
import com.metacoding.laviu._core.error.Jwt403Handler;
import com.metacoding.laviu._core.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화를 위한 BCrypt 설정
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 등록 (JWT 토큰 생성 시 필요)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 관리자용 Form 로그인 보안 설정 (높은 우선순위)
    @Bean
    @Order(1) // 우선순위 1 - 가장 먼저 처리
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 관리자 경로만 이 필터체인에서 처리
                .securityMatcher("/v1/admin/**")

                // H2 콘솔 iframe 허용 (개발 환경용 - 운영에서는 제거)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                // CSRF 활성화 (Form 로그인에서는 CSRF 보호 필요)
                .csrf(csrf -> csrf.disable()) // 개발 편의상 비활성화, 운영시 활성화 권장

                // 세션 기반 인증 사용 (관리자용)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Form 로그인 설정
                .formLogin(form -> form
                        .loginPage("/admin/login-form")        // 커스텀 로그인 페이지
                        .loginProcessingUrl("/v1/admin/login")     // 로그인 처리 URL
                        .defaultSuccessUrl("/admin/dashboard")  // 로그인 성공 후 이동할 페이지
                        .failureUrl("/admin/login-form?error")  // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/v1/admin/logout")
                        .logoutSuccessUrl("/admin/login-form?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                // 관리자 경로 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1. 인증 없이 접근 가능한 경로들 (구체적인 패턴 먼저)
                        .requestMatchers("/v1/admin/login-form", "/v1/admin/login", "/v1/admin/logout").permitAll() // 로그아웃도 인증 없이 허용

                        // 2. 관리자 권한이 필요한 나머지 경로들 (포괄적인 패턴 나중에)
                        .requestMatchers("/v1/admin/**").hasRole("ADMIN")

                        // 3. 이 필터체인에서 처리하지 않는 요청 거부
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    // JWT 토큰 기반 API 보안 설정 (중간 우선순위)
    @Bean
    @Order(2) // 우선순위 2
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // API 경로만 이 필터체인에서 처리
                .securityMatcher("/s/api/v1/**")

                // H2 콘솔 iframe 허용
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                // CSRF 비활성화 (API는 stateless이므로 불필요)
                .csrf(csrf -> csrf.disable())

                // 세션 비활성화 (JWT 토큰 사용)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Form 로그인 비활성화
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // JWT 인증 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)

                // 예외 처리 핸들러 등록
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new Jwt401Handler())  // 인증 실패 시
                        .accessDeniedHandler(new Jwt403Handler())            // 권한 부족 시
                )

                // API 경로 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/s/api/v1/users/**").hasRole("USER")
                        .requestMatchers("/s/api/v1/streams/**").hasRole("USER")
                        .requestMatchers("/s/api/v1/search/**").hasRole("USER")
                        .requestMatchers("/s/api/v1/notifications/**").hasRole("USER")
                        .requestMatchers("/s/api/v1/follows/**").hasRole("USER")
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    // 웹소켓 및 기타 요청 보안 설정 (낮은 우선순위)
    @Bean
    @Order(3) // 우선순위 3 - 마지막 처리
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 앞서 처리되지 않은 모든 요청 처리
                .securityMatcher("/**")

                // H2 콘솔 iframe 허용
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                // CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // 세션 비활성화 (웹소켓은 토큰 기반)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Form 로그인 비활성화
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // 기본 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/ws/**").permitAll() // 웹소켓 핸드셰이크 허용
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 (개발용)
                        .requestMatchers("/rtmp/**").permitAll() // ✅ nginx RTMP 콜백 허용
                        .requestMatchers("/oauth/login").permitAll() // 네이버 OAuth 로그인은 인증 없이 접근 가능하도록 허용 (로그인 전이므로)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
