package com.metacoding.laviu._core.error.ex;

import com.metacoding.laviu._core.error.ErrorEnum;

public class ExceptionApi500 extends RuntimeException {
    /**
     * 500 Internal Server Error (서버 문제)
     *
     * @param errorEnum
     */
    public ExceptionApi500(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
    }
}
