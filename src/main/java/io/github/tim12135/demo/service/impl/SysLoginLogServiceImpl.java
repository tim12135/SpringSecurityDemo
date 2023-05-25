package io.github.tim12135.demo.service.impl;

import io.github.tim12135.demo.service.SysLoginLogService;
import org.springframework.stereotype.Service;

/**
 * @author tim12135
 * @since 2023/5/25
 */
@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {
    @Override
    public void save(String username, String ipaddr) {
        // TODO: 2023/5/25 记录日志
    }
}
