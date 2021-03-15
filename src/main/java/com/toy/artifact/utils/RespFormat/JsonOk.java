package com.toy.artifact.utils.RespFormat;

public class JsonOk<T> extends JsonResp<T> {
    public JsonOk(T data) {
        super(0, "", data);
    }
}
