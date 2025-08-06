package com.metacoding.laviu._core.error.ex;

import com.metacoding.laviu._core.error.ErrorEnum;

public class ExceptionApi404 extends RuntimeException {
    /**
     * 404 Not Found (찾을 수 없음)
     *
     * @param errorEnum
     */
    public ExceptionApi404(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
    }
}
