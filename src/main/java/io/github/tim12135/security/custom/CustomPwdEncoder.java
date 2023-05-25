package io.github.tim12135.security.custom;

import io.github.tim12135.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义密码加密
 *
 * @author tim12135
 * @since 2023/5/10
 */
@Component
public class CustomPwdEncoder implements PasswordEncoder {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public String encode(CharSequence rawPassword) {
        return sysUserService.encodePwd(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(encode(rawPassword));
    }
}
