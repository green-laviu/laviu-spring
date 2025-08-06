package com.metacoding.laviu._core.error.ex;

import com.metacoding.laviu._core.error.ErrorEnum;

public class ExceptionApi401 extends RuntimeException {
    /**
     * 401 Unauthorized (인증되지 않음)
     *
     * @param errorEnum
     */
    public ExceptionApi401(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
    }
}
