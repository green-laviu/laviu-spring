package com.metacoding.laviu._core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    // 기본 시간 포맷터 (YYYY-MM-DD HH:MM)
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * LocalDateTime 객체를 "yyyy-MM-dd HH:mm" 형식의 문자열로 변환합니다.
     *
     * @param dateTime 변환할 LocalDateTime 객체
     * @return 포맷팅된 문자열. 객체가 null이면 "-"를 반환합니다.
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "-";
        }
        return dateTime.format(FORMATTER);
    }
}