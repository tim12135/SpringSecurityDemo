package io.github.tim12135.demo.service;

/**
 * 系统日志服务
 *
 * @author tim12135
 * @since 2023/5/12
 */
public interface SysLoginLogService {

    /**
     * 记录登录日志
     */
    void save(String username, String ipaddr);

}
