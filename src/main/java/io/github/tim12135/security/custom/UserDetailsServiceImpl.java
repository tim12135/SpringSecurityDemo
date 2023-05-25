package io.github.tim12135.security.custom;

import io.github.tim12135.demo.model.po.SysUser;
import io.github.tim12135.demo.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义用户信息服务实现
 *
 * @author tim12135
 * @since 2023/5/15
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户" + username + "不存在");
        }
        List<String> permissions = new ArrayList<>();
        if (Objects.equals(sysUser.getLocked(), 0) && Objects.equals(sysUser.getStatus(), 1)) {
            permissions.addAll(sysUserService.getPermission(sysUser.getId()));
        }
        CustomUser customUser = new CustomUser();
        BeanUtils.copyProperties(sysUser, customUser);
        customUser.setPermissions(permissions);
        return customUser;
    }
}
