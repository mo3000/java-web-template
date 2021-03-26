package com.toy.artifact.request.admin.admin;

import com.toy.artifact.request.PaginatorRequest;

public class AdminListRequest extends PaginatorRequest {
    private String username;
    private String realname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
