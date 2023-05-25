package io.github.tim12135.security;

import io.github.tim12135.demo.common.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 通用异常处理
 *
 * @author tim12135
 * @since 2023/5/11
 */
@Component
@RestControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    public R<Void> handle(AccessDeniedException e) {
        throw e;
    }
}
