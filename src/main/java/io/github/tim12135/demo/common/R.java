package io.github.tim12135.demo.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tim12135
 * @since 2023/4/28
 */
@Data
public class R<T> {
    private boolean success;
    private T data;

    private List<ErrorInfo> errorInfos;

    public R() {
    }

    public static <T> R<T> ok() {
        R<T> r = new R<>();
        r.setSuccess(true);
        r.setData(null);
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static R<Void> fail() {
        R<Void> r = new R<>();
        r.setSuccess(false);
        return r;
    }

    public R<T> addError(String errorCode, String errorMsg) {
        if (errorInfos == null) {
            errorInfos = new ArrayList<>();
        }
        errorInfos.add(new ErrorInfo(errorCode, errorMsg));
        return this;
    }

    public R<T> addError(String errorCode, String errorMsg, List<Object> args) {
        if (errorInfos == null) {
            errorInfos = new ArrayList<>();
        }
        errorInfos.add(new ErrorInfo(errorCode, errorMsg, args));
        return this;
    }

    public R<T> addError(ErrorInfo errorInfo) {
        if (errorInfos == null) {
            errorInfos = new ArrayList<>();
        }
        errorInfos.add(errorInfo);
        return this;
    }
}
