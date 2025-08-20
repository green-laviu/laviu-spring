package com.metacoding.laviu._core.interceptor;

import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu._core.utils.StreamKeyUtil;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final StreamsService streamService; // 스트림 소유권 확인 서비스

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.debug("=== 인터셉터 진입 ===");
        log.debug("Command: {}", accessor.getCommand());
        log.debug("Destination: {}", accessor.getDestination());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader(JwtUtil.HEADER);
            log.debug("받은 토큰: {}", token);

            Users user = safelyVerifyToken(token);
            if (user != null) {
                log.debug("토큰 검증 성공, id: {}", user.getId());
                log.debug("세션에 저장 중");
                accessor.setUser(new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                ));
                log.debug("세션에 저장 성공");
            } else {
                log.debug("토큰 검증 실패!");
                throw new AccessDeniedException("유효하지 않은 토큰입니다. 연결을 거부합니다.");
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.debug("구독 요청 처리 중...");
            if (accessor.getUser() == null) {
                log.debug("인증 정보 없음!");
                throw new AccessDeniedException("인증되지 않은 사용자입니다.");
            }

            Users user = (Users) ((Authentication) accessor.getUser()).getPrincipal();
            String destination = accessor.getDestination();
            log.debug("사용자: {}, 구독 대상: {}", user.getId(), destination);
            String streamKey = StreamKeyUtil.extractStreamKeyFromDestination(destination);
            log.debug("스트림 키: {}", streamKey);

            if (destination != null && destination.contains("/participants")) {
                if (!streamService.isStreamOwner(streamKey, user.getId())) {
                    log.warn("스트리머가 아닌 사용자가 전용 채널에 접근 시도: {}", user.getId());
                    throw new AccessDeniedException("스트리머만 구독할 수 있는 채널입니다.");
                }
            }
        }
        log.debug("=== 인터셉터 통과 ===");
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
            log.debug("JWT 검증 실패: {}", e.getMessage());
            return null;
        }
    }
}
