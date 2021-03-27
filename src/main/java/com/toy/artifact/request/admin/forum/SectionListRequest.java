package com.toy.artifact.request.admin.forum;

import com.toy.artifact.request.PaginatorRequest;

public class SectionListRequest extends PaginatorRequest {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
