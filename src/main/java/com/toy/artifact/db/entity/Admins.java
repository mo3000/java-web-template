package com.toy.artifact.db.entity;


import com.toy.artifact.db.vo.HasId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Admins implements HasId {
    public void setId(Long id) {
        this.id = id;
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String realname;

    private Integer status;

    private LocalDateTime createdAt;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Admins() {}

    public Admins(Long id, String username, String password, String realname, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.realname = realname;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getRealname() {
        return realname;
    }

    public Integer getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Admins admins = (Admins) o;

        return id.equals(admins.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
