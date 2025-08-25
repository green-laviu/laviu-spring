package com.metacoding.laviu._core.error.ex;

import com.metacoding.laviu._core.error.ErrorEnum;

/**
 * 401 Unauthorized (인증되지 않음) - STOMP 통신 전용 예외
 * HTTP 응답이 아닌 STOMP 에러 메시지 전송을 목적으로 합니다.
 */
public class StompException401 extends RuntimeException {
    public StompException401(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
    }
}