package com.metacoding.laviu._core.error;

public enum ErrorEnum {
    // 나중에 수정해서 사용함

    /**
     * 400 Bad Request - 이미 응답된 요청입니다.
     */
    INVALID_INVITE_RESPONSE_STATE(400, "이미 응답된 요청입니다"),

    /**
     * 400 Bad Request - 잘못된 형식의 토큰으로 요청이 들어왔습니다.
     */
    INVALID_TOKEN_FORMAT(400, "잘못된 형식의 토큰으로 요청이 들어왔습니다"),

    /**
     * 400 Bad Request - 이미 진행 중인 방송이 존재합니다.
     */
    ALREADY_LIVE_STREAMING(400, "이미 진행 중인 방송이 존재합니다."),

    /**
     * 401 Unauthorized - 로그인이 필요합니다.
     */
    LOGIN_REQUIRED(401, "로그인이 필요합니다"),

    /**
     * 401 Unauthorized - 관리자 권한이 필요합니다.
     */
    ADMIN_PRIVILEGE_REQUIRED(401, "관리자 권한이 필요합니다"),

    /**
     * 401 Unauthorized - 토큰이 만료되었습니다.
     */
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다"),

    /**
     * 401 Bad Request - 유효하지 않은 토큰입니다.
     */
    INVALID_TOKEN(401, "유효하지 않는 토큰입니다"),

    /**
     * 401 Bad Request - Authorization 헤더에 'Bearer'가 누락되었습니다.
     */
    BEARER_PREFIX_MISSING(401, "Authorization 헤더에 'Bearer'가 누락되었습니다"),

    /**
     * 401 Unauthorized - 토큰이 존재하지 않습니다.
     */
    TOKEN_NOT_FOUND(401, "토큰이 존재하지 않습니다"),

    /**
     * 403 Forbidden - 접근 권한이 없습니다.
     */
    ACCESS_DENIED(403, "접근 권한이 없습니다."),

    /**
     * 404 Not Found - 해당 방송이 존재하지 않습니다.
     */
    NO_MATCH_STREAMER_ID_AND_USER_ID(403, "해당 방송을 하는 방송인이 아닙니다."),

    /**
     * 404 Not Found - 관리자가 존재하지 않습니다.
     */
    NOT_MY_FRIEND(404, "서로 친구가 아닙니다"),

    /**
     * 404 Not Found - 해당 유저가 존재하지 않습니다.
     */
    NOT_FOUND_USER(404, "해당 유저를 찾을 수 없습니다."),

    /**
     * 404 Not Found - 해당 방송이 존재하지 않습니다.
     */
    NOT_FOUND_STREAM(404, "해당 방송을 찾을 수 없습니다."),
    
    /**
     * 500 Internal Server Error - 알 수 없는 오류 발생 시 기본 메시지입니다.
     */
    INTERNAL_SERVER_ERROR(500, "알 수 없는 오류가 발생했습니다. 관리자에게 문의해주세요"),

    /**
     * [신규 추가]
     * 500 Internal Server Error - 데이터베이스에 저장된 값이 코드와 일치하지 않을 때 발생합니다.
     * 예: DB의 뱃지 타입 '월간기록'을 Java Enum으로 변환하려 할 때, 해당 Enum 상수가 없는 경우.
     */
    INVALID_DATABASE_DATA(500, "서버 데이터에 문제가 발생했습니다. 관리자에게 문의해주세요.");

    /**
     * HTTP 상태 코드
     */
    private final int status;

    /**
     * 클라이언트에 보여줄 에러 메시지
     */
    private final String message;

    /**
     * 생성자 - 상태 코드와 메시지를 설정합니다.
     *
     * @param status  HTTP 상태 코드
     * @param message 사용자에게 전달할 에러 메시지
     */
    ErrorEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * HTTP 상태 코드를 반환합니다.
     *
     * @return HTTP 상태 코드 (예: 400, 401, 404 등)
     */
    public int getStatus() {
        return status;
    }

    /**
     * 에러 메시지를 반환합니다.
     *
     * @return 클라이언트에 보여줄 에러 메시지
     */
    public String getMessage() {
        return message;
    }
}
