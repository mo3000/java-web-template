package com.toy.artifact.db.repo;

import com.toy.artifact.db.entity.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CrudRepository<Roles, Long> {

}
