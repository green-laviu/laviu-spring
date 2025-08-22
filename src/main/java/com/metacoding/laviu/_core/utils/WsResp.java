package com.metacoding.laviu._core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * ✅ WebSocket STOMP 응답 객체 (Generic)
 *
 * <p>
 * STOMP 메시지 핸들러에서 일관된 응답 포맷을 제공하기 위한 클래스입니다.
 * HTTP 응답의 Resp 객체와 유사하게 status, msg, data 세 가지 필드를 포함합니다.
 * </p>
 *
 * @param <T> 응답 데이터의 타입
 */
@Data
@NoArgsConstructor // 역직렬화를 위해 필요합니다.
@AllArgsConstructor
public class WsResp<T> {
    /**
     * 응답 상태 코드 (예: 200, 400 등)
     */
    private HttpStatus status;

    /**
     * 응답 메시지 (예: "성공", "잘못된 요청" 등)
     */
    private String msg;

    /**
     * 응답 데이터 본문
     */
    private T data;

    /**
     * ✅ 성공 응답을 생성하는 정적 메서드
     *
     * @param data 실제 응답 데이터
     * @param <B>  응답 데이터의 타입
     * @return WsResp 객체
     */
    public static <B> WsResp<B> ok(B data) {
        return new WsResp<>(HttpStatus.OK, "성공", data);
    }

    /**
     * ❌ 실패 응답을 생성하는 정적 메서드
     *
     * @param status 실패 상태 코드 (예: 400)
     * @param msg    에러 메시지
     * @return WsResp 객체
     */
    public static WsResp<?> fail(HttpStatus status, String msg) {
        return new WsResp<>(status, msg, null);
    }
}