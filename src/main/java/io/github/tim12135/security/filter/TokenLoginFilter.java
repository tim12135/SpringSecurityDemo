package io.github.tim12135.security.filter;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tim12135.demo.common.IpUtils;
import io.github.tim12135.demo.common.R;
import io.github.tim12135.demo.model.po.SysUser;
import io.github.tim12135.demo.model.vo.LoginVO;
import io.github.tim12135.demo.service.SysLoginLogService;
import io.github.tim12135.demo.service.SysUserService;
import io.github.tim12135.security.ResponseUtils;
import io.github.tim12135.security.WebSecurityConfig;
import io.github.tim12135.security.custom.CustomUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录实现过滤器
 *
 * @author tim12135
 * @since 2023/5/10
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private StringRedisTemplate redisTemplate;

    private Integer maxLoginAttempts;

    private Integer tokenTimeoutSeconds;

    private SysUserService sysUserService;

    private SysLoginLogService sysLoginLogService;

    public TokenLoginFilter(AuthenticationManager authenticationManager, StringRedisTemplate redisTemplate,
                            SysUserService sysUserService, SysLoginLogService sysLoginLogService,
                            Integer maxLoginAttempts, Integer tokenTimeoutSeconds) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(WebSecurityConfig.LOGIN_URI, "POST"));
        this.redisTemplate = redisTemplate;
        this.sysUserService = sysUserService;
        this.sysLoginLogService = sysLoginLogService;
        this.maxLoginAttempts = maxLoginAttempts;
        this.tokenTimeoutSeconds = tokenTimeoutSeconds;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            LoginVO loginVo = new ObjectMapper().readValue(req.getInputStream(), LoginVO.class);
            req.setAttribute(WebSecurityConfig.ATTR_USERNAME, loginVo.getUsername());
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        //获取认证信息
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        //缓存认证信息到redis
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(customUser), tokenTimeoutSeconds, TimeUnit.SECONDS);
        //解锁用户
        sysUserService.releaseLock(customUser.getId());
        //记录登录日志
        sysLoginLogService.save(customUser.getUsername(), IpUtils.getIpAddress(request));
        //返回
        ResponseUtils.out(response, R.ok(token));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException e) throws IOException, ServletException {

        if (e instanceof BadCredentialsException) {
            String userName = (String) request.getAttribute(WebSecurityConfig.ATTR_USERNAME);
            if (StringUtils.isNotBlank(userName)) {
                SysUser sysUser = sysUserService.getByUsername(userName);
                if (sysUser != null) {
                    sysUser.setErrorNum(Optional.ofNullable(sysUser.getErrorNum()).orElse(0) + 1);
                    if (sysUser.getErrorNum() >= maxLoginAttempts) {
                        sysUser.setLocked(1);
                    }
                    sysUserService.updateLock(sysUser);
                }
            }
        }
        ResponseUtils.out(response, R.fail().addError("401", e.getMessage()));
    }
}
