package io.github.tim12135.security.custom;

import io.github.tim12135.demo.common.R;
import io.github.tim12135.security.ResponseUtils;
import io.github.tim12135.security.WebSecurityConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登出实现
 *
 * @author tim12135
 * @since 2023/5/12
 */
public class CustomLogoutHandler implements LogoutHandler {
    private StringRedisTemplate redisTemplate;

    public CustomLogoutHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader(WebSecurityConfig.CONTEXT_TOKEN);
        redisTemplate.delete(token);
        ResponseUtils.out(response, R.ok("logout successfully, old token " + token + " deleted"));

    }
}
