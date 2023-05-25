package io.github.tim12135.security.custom;

import io.github.tim12135.demo.common.R;
import io.github.tim12135.security.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义403异常处理
 *
 * @author tim12135
 * @since 2023/5/11
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtils.out(response, R.fail().addError("403", accessDeniedException.getMessage()));
    }
}
