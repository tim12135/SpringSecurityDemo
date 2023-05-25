package io.github.tim12135.demo.service;

import io.github.tim12135.demo.model.po.SysUser;

import java.util.List;

/**
 * 用户服务
 *
 * @author tim12135
 * @since 2023/5/4
 */
public interface SysUserService {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getByUsername(String username);

    /**
     * 锁定用户
     *
     * @param sysUser 用户
     */
    void updateLock(SysUser sysUser);

    /**
     * 释放锁
     *
     * @param id 用户id
     */
    void releaseLock(String id);

    /**
     * 获取用户权限
     *
     * @param id 用户id
     * @return 权限
     */
    List<String> getPermission(String id);

    /**
     * 密码加密
     *
     * @param pwd 密码
     * @return 加密后密码
     */
    String encodePwd(String pwd);

}
