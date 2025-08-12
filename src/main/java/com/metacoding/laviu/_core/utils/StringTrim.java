package com.metacoding.laviu._core.utils;

/**
 * 문자열의 공백을 정규화하는 유틸리티
 * - null 입력 시 null 반환
 * - 문자열 앞뒤의 모든 공백 제거
 * - 중간의 2개 이상 연속된 공백을 하나의 공백으로 변환
 * 예시:
 * "  Hello   World  " -> "Hello World"
 *
 * @param "input" 입력 문자열
 * @return 공백이 정규화된 문자열
 */
public class StringTrim {

    public static String normalizeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\s{2,}", " ");
    }
}
