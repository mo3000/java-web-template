package com.toy.artifact.db.entity;

import com.toy.artifact.db.composedkey.AdminRoleId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class AdminRole {

    @Column(insertable = false, updatable = false)
    private Long adminid;

    @Column(insertable = false, updatable = false)
    private Long roleid;

    @EmbeddedId
    private AdminRoleId id;

    public AdminRole() { }


    public Long getAdminid() {
        return adminid;
    }

    public Long getRoleid() {
        return roleid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminRole adminRole = (AdminRole) o;

        return id.equals(adminRole.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public AdminRole(Long adminid, Long roleid) {
        this.adminid = adminid;
        this.roleid = roleid;
        id = new AdminRoleId(adminid, roleid);
    }


}
