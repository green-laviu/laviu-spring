package com.metacoding.laviu._core.error;

import com.metacoding.laviu._core.error.ex.*;
import com.metacoding.laviu._core.utils.WsResp;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * STOMP 통신 중 발생하는 예외를 전역적으로 처리하는 핸들러입니다.
 *
 * @MessageMapping 핸들러에서 던져진 예외를 가로채서 STOMP ERROR 프레임으로 변환하여 클라이언트에게 전송합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StompExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageExceptionHandler(StompException400.class)
    public void onBadRequestException(StompException400 e, SimpMessageHeaderAccessor accessor) {
        log.warn("STOMP 400 Bad Request 예외 발생: {}", e.getMessage());
        sendErrorMessage(accessor, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @MessageExceptionHandler(StompException401.class)
    public void onUnauthorizedException(StompException401 e, SimpMessageHeaderAccessor accessor) {
        log.warn("STOMP 401 Unauthorized 예외 발생: {}", e.getMessage());
        sendErrorMessage(accessor, e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @MessageExceptionHandler(StompException403.class)
    public void onForbiddenException(StompException403 e, SimpMessageHeaderAccessor accessor) {
        log.warn("STOMP 403 Forbidden 예외 발생: {}", e.getMessage());
        sendErrorMessage(accessor, e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @MessageExceptionHandler(StompException404.class)
    public void onNotFoundException(StompException404 e, SimpMessageHeaderAccessor accessor) {
        log.warn("STOMP 404 Not Found 예외 발생: {}", e.getMessage());
        sendErrorMessage(accessor, e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @MessageExceptionHandler(StompException500.class)
    public void onInternalServerError(StompException500 e, SimpMessageHeaderAccessor accessor) {
        log.error("STOMP 500 Internal Server Error 예외 발생: {}", e.getMessage());
        sendErrorMessage(accessor, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @MessageExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, SimpMessageHeaderAccessor accessor) {
        log.warn("STOMP 유효성 검사 예외 발생");
        String errorMessage = e.getConstraintViolations().iterator().next().getMessage();
        sendErrorMessage(accessor, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @MessageExceptionHandler(Exception.class)
    public void onUnknownException(Exception e, SimpMessageHeaderAccessor accessor) {
        log.error("스택 트레이스 시작");
        log.error("STOMP 알 수 없는 오류 발생", e);
        log.error("스택 트레이스 끝");
        sendErrorMessage(accessor, "알 수 없는 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * STOMP ERROR 프레임을 클라이언트에게 전송하는 공통 메서드입니다.
     *
     * @param accessor     STOMP 메시지 헤더 접근자
     * @param errorMessage 클라이언트에게 보낼 에러 메시지
     */
    private void sendErrorMessage(SimpMessageHeaderAccessor accessor, String errorMessage, HttpStatus errorCode) {
        String sessionId = accessor.getSessionId();
        Principal principal = accessor.getUser();
        WsResp<?> wsResp = WsResp.fail(errorCode, errorMessage);

        if (principal != null) {
            // 사용자 인증 정보가 있는 경우
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    "/queue/errors",
                    wsResp
            );
        } else if (sessionId != null) {
            // 사용자 인증 정보가 없는 경우 (익명 사용자)
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/errors",
                    wsResp
            );
        } else {
            // 어떤 식별자도 없는 경우, 로그만 남김
            log.error("STOMP 세션 또는 사용자 ID를 찾을 수 없어 에러 메시지를 보낼 수 없습니다.");
        }
    }
}
