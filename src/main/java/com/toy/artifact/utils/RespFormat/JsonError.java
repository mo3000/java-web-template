package com.toy.artifact.utils.RespFormat;

import java.util.Optional;

public class JsonError<T> extends JsonResp<T> {

    public JsonError(String msg) {
        super(1, msg, null);
    }
}
