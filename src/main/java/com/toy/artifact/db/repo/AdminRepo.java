package com.toy.artifact.db.repo;

import com.toy.artifact.db.entity.Admins;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends CrudRepository<Admins, Long> {
    public boolean existsByUsername(String username);

    public Admins findByUsername(String username);

    @Modifying
    @Query(value = "update Admins set status=(status+1) % 2 where id=:id")
    public void toggleStatus(Long id);
}
