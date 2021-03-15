package com.toy.artifact.db.vo;

import java.time.LocalDateTime;
import java.util.List;

public class AdminVo implements HasId {
    private Long id;
    private LocalDateTime created_at;
    private String username;
    private Integer status;
    private String realname;
    private List<RolesVo> roles;

    public List<RolesVo> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesVo> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }


    public AdminVo() {}
}
