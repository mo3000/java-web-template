package com.toy.artifact.utils.RespFormat;

public class JsonError extends JsonResp<Object> {
    public JsonError(String msg) {
        super(1, msg, null);
    }
}
