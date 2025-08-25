package com.metacoding.laviu._core.utils;

public final class EmailUtil {

    private EmailUtil() {
    }

    /**
     * 이메일에서 @ 앞부분(local-part)을 추출한다. 플러스 태깅은 제거한다. 예) "ab+cd@gmail.com" -> "ab"
     */
    public static String localPart(String email) {
        if (email == null) throw new IllegalArgumentException("email is null");
        String trimmed = email.trim();
        int at = trimmed.indexOf('@');
        if (at <= 0) throw new IllegalArgumentException("invalid email: " + email);

        String local = trimmed.substring(0, at);
        int plus = local.indexOf('+');
        if (plus > -1) local = local.substring(0, plus);
        return local;
    }
}