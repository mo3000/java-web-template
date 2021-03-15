package com.toy.artifact.utils.RespFormat;

public class JsonResp<T> {
    private final int code;
    private final String msg;
    private final T data;

    public JsonResp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
