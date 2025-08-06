package com.metacoding.laviu._core.error.ex;

import com.metacoding.laviu._core.error.ErrorEnum;

public class ExceptionApi403 extends RuntimeException {
    /**
     * 401 Forbidden (접근 금지 및 권한 없음)
     *
     * @param errorEnum
     */
    public ExceptionApi403(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
    }
}
