package com.metacoding.laviu._core.interceptor;

import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu._core.utils.StreamKeyUtil;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private StreamsService streamService; // 스트림 소유권 확인 서비스

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // [디버깅] 명령어/대상 출력
        System.out.println("=== 인터셉터 진입 ===");
        System.out.println("Command: " + accessor.getCommand());
        System.out.println("Destination: " + accessor.getDestination());

        // 1. CONNECT 시: JWT 인증 및 세션에 사용자 정보 등록
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader(JwtUtil.HEADER);
            System.out.println("받은 토큰: " + token);

            Users user = safelyVerifyToken(token);
            if (user != null) {
                System.out.println("토큰 검증 성공, id: " + user.getId());
                // 세션에 사용자 인증 정보 등록(권한 정보는 ROLE 문자열에서 필요시 파싱)
                System.out.println("세션에 저장 중");
                accessor.setUser(new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                ));
                System.out.println("세션에 저장 성공");
            } else {
                System.out.println("토큰 검증 실패!");
                throw new AccessDeniedException("유효하지 않은 토큰입니다. 연결을 거부합니다.");
            }
        }

        // 2. SUBSCRIBE 시: 채널별 구독 권한 검사
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("구독 요청 처리 중...");
            if (accessor.getUser() == null) {
                System.out.println("인증 정보 없음!");
                throw new AccessDeniedException("인증되지 않은 사용자입니다.");
            }

            Users user = (Users) ((Authentication) accessor.getUser()).getPrincipal();
            String destination = accessor.getDestination();
            System.out.println("사용자: " + user.getId() + ", 구독 대상: " + destination);
            String streamKey = StreamKeyUtil.extractStreamKeyFromDestination(destination);
            System.out.println("스트림 키" + streamKey);

            // 2-A. 스트리머 전용 채널('/participants') 구독 시 → 소유권 검사
            if (destination != null && destination.contains("/participants")) {
                if (!streamService.isStreamOwner(streamKey, user.getId())) {
                    throw new AccessDeniedException("스트리머만 구독할 수 있는 채널입니다.");
                }
            }
            // 2-B. 채팅 채널은 사용자 인증만 있으면 구독 가능(추가 검사 없음)
        }
        System.out.println("=== 인터셉터 통과 ===");
        return message;
    }

    // --- JWT 토큰 실제 검증 및 객체 변환 ---
    private Users safelyVerifyToken(String token) {
        if (token == null || !token.startsWith(JwtUtil.TOKEN_PREFIX)) {
            return null;
        }
        try {
            String pureToken = token.substring(JwtUtil.TOKEN_PREFIX.length());
            return JwtUtil.verify(pureToken); // 서명·만료 검증 및 클레임 추출
        } catch (Exception e) {
            System.out.println("JWT 검증 실패: " + e.getMessage());
            return null;
        }
    }
}
