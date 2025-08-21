package com.metacoding.laviu._core.error.ex;

import com.metacoding.laviu._core.error.ErrorEnum;

public class ExceptionApi400 extends RuntimeException {
    /**
     * 400 Bad Request (잘못된 요청)
     *
     * @param errorEnum
     */
    public ExceptionApi400(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
    }

    /**
     * 400 Bad Request (잘못된 요청)
     * <p>
     * GlobalValidationHandler 용
     *
     * @param message
     */
    public ExceptionApi400(String message) {
        super(message);
    }
}
