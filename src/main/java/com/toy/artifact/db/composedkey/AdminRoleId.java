package com.toy.artifact.db.composedkey;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AdminRoleId implements Serializable {

    private Long adminid;

    private Long roleid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminRoleId that = (AdminRoleId) o;

        if (!adminid.equals(that.adminid)) return false;
        return roleid.equals(that.roleid);
    }

    @Override
    public int hashCode() {
        int result = adminid.hashCode();
        result = 31 * result + roleid.hashCode();
        return result;
    }

    public Long getAdminid() {
        return adminid;
    }

    public void setAdminid(Long adminid) {
        this.adminid = adminid;
    }

    public void setRoleid(Long roleid) {
        this.roleid = roleid;
    }

    public Long getRoleid() {
        return roleid;
    }

    public AdminRoleId(Long adminid, Long roleid) {
        this.adminid = adminid;
        this.roleid = roleid;
    }

    public AdminRoleId() {}
}
