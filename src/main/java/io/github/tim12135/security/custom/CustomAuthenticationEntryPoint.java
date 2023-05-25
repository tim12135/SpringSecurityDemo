package io.github.tim12135.security.custom;

import io.github.tim12135.demo.common.R;
import io.github.tim12135.security.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义401异常处理
 *
 * @author tim12135
 * @since 2023/5/11
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtils.out(response, R.fail().addError("401", authException.getMessage()));
    }
}
