package com.toy.artifact.db.repo;

import com.toy.artifact.db.entity.Admins;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends CrudRepository<Admins, Long> {
    public boolean existsByUsername(String username);
}
