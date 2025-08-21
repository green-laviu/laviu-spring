package com.metacoding.laviu._core.error;

import com.metacoding.laviu._core.error.ex.StompException400;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * 이 클래스는 @MessageMapping 메서드의 매개변수에 대한 유효성 검사를
 * AOP(Aspect-Oriented Programming) 방식으로 처리하는 핸들러입니다.
 * <p>
 * 이 방식은 HTTP @Controller에서 BindingResult를 사용하는 방식과 유사합니다.
 * 그러나 STOMP에서는 Spring의 @Validated 어노테이션을 사용하여
 * 자동으로 ConstraintViolationException 예외를 발생시키고,
 * 이 예외를 StompExceptionHandler에서 중앙 집중적으로 처리하는 것이 훨씬 효율적입니다.
 * 따라서 이 핸들러는 교육적인 목적으로만 사용하고, 실제 구현에서는 @Validated를 사용하는 것을 권장합니다.
 */
@Slf4j
@Aspect
@Component
public class StompValidationHandler {

    // @MessageMapping이 붙은 메서드가 실행되기 직전에 이 어드바이스를 호출합니다.
    @Before("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void validateMessage(JoinPoint jp) {
        log.debug("STOMP 유효성 검사 어드바이스 진입");

        // 메서드의 모든 매개변수를 순회합니다.
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof Errors) {
                Errors errors = (Errors) arg;

                // 유효성 검사 에러가 있는지 확인합니다.
                if (errors.hasErrors()) {
                    List<FieldError> fieldErrors = errors.getFieldErrors();
                    for (FieldError fieldError : fieldErrors) {
                        // 첫 번째 에러 메시지를 포함한 커스텀 예외를 던집니다.
                        // 이 예외는 StompExceptionHandler에 의해 처리됩니다.
                        String errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
                        log.warn("STOMP 유효성 검사 실패: {}", errorMessage);
                        throw new StompException400(errorMessage);
                    }
                }
            }
        }
    }
}
