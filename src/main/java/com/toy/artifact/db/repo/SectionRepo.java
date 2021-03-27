package com.toy.artifact.db.repo;

import com.toy.artifact.db.entity.Section;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SectionRepo extends CrudRepository<Section, Long> {

    @Transactional
    @Modifying
    @Query("update Section set status=(status + 1)%2 where id=:id")
    public void toggleStatus(Long id);

    @Transactional
    @Modifying
    @Query("update Section set name=:name, description=:description, sort=:sort where id=:id")
    public void update(Long id, String name, String description, Integer sort);

    @Transactional
    @Modifying
    @Query("update Section set sort=:sort where id=:id")
    public void updateSort(Long id, Integer sort);
}
