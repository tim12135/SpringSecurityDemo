package io.github.tim12135.security.custom;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.tim12135.demo.model.po.SysUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * web security 用户信息
 *
 * @author tim12135
 * @since 2023/5/10
 */
@Getter
@Setter
public class CustomUser extends SysUser implements UserDetails, CredentialsContainer {

    private List<String> permissions;

    @Override
    public void eraseCredentials() {
        setPassword(null);
    }

    @JSONField(serialize = false, deserialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String authority : permissions) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return simpleGrantedAuthorities;
    }

    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(getLocked(), 0);
    }

    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isEnabled() {
        return Objects.equals(getStatus(), 1);
    }
}
