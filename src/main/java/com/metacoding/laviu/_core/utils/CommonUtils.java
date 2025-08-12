package com.metacoding.laviu._core.utils;

import java.util.UUID;


public class CommonUtils {

    private CommonUtils() {
    } // 인스턴스 생성 방지

    // 📌 이메일 로컬 파트 추출
    public static String localPart(String email) {
        if (email == null) return null;
        int at = email.indexOf('@');
        return (at > 0) ? email.substring(0, at) : email;
    }

    // 📌 스트림 키 생성
    public static String generateStreamKey() {
        return UUID.randomUUID().toString();
    }
}