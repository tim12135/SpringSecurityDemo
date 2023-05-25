package io.github.tim12135.demo.model.po;

import lombok.Data;

/**
 * @author tim12135
 * @since 2023/5/25
 */
@Data
public class SysUser {
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    private String name;

    private String mobile;

    private String avatar;

    /**
     * 状态，0 禁用， 1 正常
     */
    private Integer status;

    /**
     * 锁定：0 否，1 是
     */
    private Integer locked;

    /**
     * 登录错误次数
     */
    private Integer errorNum;
}
