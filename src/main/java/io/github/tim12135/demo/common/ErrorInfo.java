package io.github.tim12135.demo.common;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author tim12135
 * @since 2023/4/28
 */
@Getter
@Setter
public class ErrorInfo {
    private String code;
    private String msg;
    private List<Object> args;

    public ErrorInfo() {
    }

    public ErrorInfo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorInfo(String code, String msg, List<Object> args) {
        this.code = code;
        this.msg = msg;
        this.args = args;
    }
}
