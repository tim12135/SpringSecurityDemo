package io.github.tim12135.security.filter;

import com.alibaba.fastjson2.JSONObject;
import io.github.tim12135.security.WebSecurityConfig;
import io.github.tim12135.security.custom.CustomUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 验证Token过滤器
 *
 * @author tim12135
 * @since 2023/5/10
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private StringRedisTemplate redisTemplate;

    private Integer tokenTimeoutSeconds;

    public TokenAuthenticationFilter(StringRedisTemplate redisTemplate, Integer tokenTimeoutSeconds) {
        this.redisTemplate = redisTemplate;
        this.tokenTimeoutSeconds = tokenTimeoutSeconds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("uri:" + request.getRequestURI());
        //如果是登录接口，直接放行
        if (WebSecurityConfig.LOGIN_URI.equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader(WebSecurityConfig.CONTEXT_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            String authoritiesString = redisTemplate.opsForValue().get(token);
            if (StringUtils.isNotBlank(authoritiesString)) {
                try {
                    CustomUser customUser = JSONObject.parseObject(authoritiesString, CustomUser.class);
                    if (customUser != null) {
                        redisTemplate.expire(token, tokenTimeoutSeconds, TimeUnit.SECONDS);
                        return new UsernamePasswordAuthenticationToken(customUser.getUsername(), null, customUser.getAuthorities());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }
}
