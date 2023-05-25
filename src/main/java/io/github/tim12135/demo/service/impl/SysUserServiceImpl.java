package io.github.tim12135.demo.service.impl;

import io.github.tim12135.demo.model.po.SysUser;
import io.github.tim12135.demo.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author tim12135
 * @since 2023/5/25
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    // mock
    SysUser sysUser;

    public SysUserServiceImpl() {
        this.sysUser = new SysUser();
        sysUser.setId("1");
        sysUser.setUsername("admin");
        sysUser.setPassword(encodePwd("123456"));
        sysUser.setLocked(0);
        sysUser.setStatus(1);
        sysUser.setErrorNum(0);
        sysUser.setName("admin");
    }

    @Override
    public SysUser getByUsername(String username) {
        // mock
        return sysUser;
    }

    @Override
    public void updateLock(SysUser sysUser) {
        // mock
        sysUser.setLocked(sysUser.getLocked());
        sysUser.setErrorNum(sysUser.getErrorNum());
    }

    @Override
    public void releaseLock(String id) {
        // mock
        sysUser.setLocked(0);
        sysUser.setErrorNum(0);
    }

    @Override
    public List<String> getPermission(String id) {
        // mock
        return Arrays.asList("sys.menu.view");
    }

    @Override
    public String encodePwd(String pwd) {
        // mock
        return DigestUtils.md5DigestAsHex(pwd.getBytes(StandardCharsets.UTF_8));
    }
}
