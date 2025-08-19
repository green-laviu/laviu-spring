package com.metacoding.laviu._core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamKeyUtil {
    /**
     * 구독 목적지 주소에서 스트림 키를 추출하는 헬퍼 메서드
     *
     * @param destination /sub/{streamKey}/... 형식의 주소
     * @return 추출된 streamKey
     */
    public static String extractStreamKeyFromDestination(String destination) {
        if (destination == null) return null;
        // 정규표현식을 사용하여 /sub/스트림키/어딘가 에서 '스트림키' 부분 추출
        Pattern pattern = Pattern.compile("/sub/streams/([^/]+)/.*");
        Matcher matcher = pattern.matcher(destination);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
