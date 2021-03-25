package com.toy.artifact.db.repo;

import com.toy.artifact.db.composedkey.AdminRoleId;
import com.toy.artifact.db.entity.AdminRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminRoleRepo extends CrudRepository<AdminRole, AdminRoleId> {

    @Modifying
    @Query("delete from AdminRole where adminid=:adminid and roleid in :roles")
    public void deleteAll(Long adminid, Iterable<Long> roles);

}
