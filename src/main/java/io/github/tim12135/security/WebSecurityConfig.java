package io.github.tim12135.security;

import io.github.tim12135.security.custom.CustomAccessDeniedHandler;
import io.github.tim12135.security.custom.CustomAuthenticationEntryPoint;
import io.github.tim12135.security.custom.CustomLogoutHandler;
import io.github.tim12135.security.custom.CustomPwdEncoder;
import io.github.tim12135.security.filter.TokenAuthenticationFilter;
import io.github.tim12135.security.filter.TokenLoginFilter;
import io.github.tim12135.demo.service.SysLoginLogService;
import io.github.tim12135.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * web security 配置
 *
 * @author tim12135
 * @since 2023/5/10
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String LOGIN_URI = "/admin/system/index/login";
    public static final String LOGOUT_URI = "/admin/system/index/logout";
    public static final String ATTR_USERNAME = "username";
    public static final String CONTEXT_TOKEN = "m-token";
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Autowired
    private CustomPwdEncoder customMd5PasswordEncoder;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${demo.admin.maxLoginAttempts:3}")
    private Integer maxLoginAttempts;

    @Value("${demo.admin.tokenTimeoutSeconds:600}")
    private Integer tokenTimeoutSeconds;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 这是配置的关键，决定哪些接口开启防护，哪些接口绕过防护
        http
                //关闭csrf，开启cors跨域
                .csrf().disable()
                .cors()

                //禁用session
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_URI).permitAll()
                .anyRequest().authenticated()

                //登出配置
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URI, "POST"))
                .addLogoutHandler(new CustomLogoutHandler(redisTemplate))

                // 异常处理
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())

                //过滤器
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate, tokenTimeoutSeconds), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new TokenLoginFilter(authenticationManager(), redisTemplate, sysUserService, sysLoginLogService,
                        maxLoginAttempts, tokenTimeoutSeconds));

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定UserDetailService和加密器
        auth
                .eraseCredentials(true)
                .userDetailsService(userDetailsService)
                .passwordEncoder(customMd5PasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/favicon.ico", "/swagger-resources/**", "/webjars/**", "/v3/**", "/swagger-ui/**", "/swagger-ui**", "/doc.html", "/error");
    }
}
