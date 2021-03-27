package com.toy.artifact.db.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.artifact.db.entity.QSection;
import com.toy.artifact.db.entity.Section;
import com.toy.artifact.db.repo.SectionRepo;
import com.toy.artifact.utils.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ForumService {

    public ForumService(SectionRepo sectionRepo, JPAQueryFactory queryFactory) {
        this.sectionRepo = sectionRepo;
        this.queryFactory = queryFactory;
    }

    private final SectionRepo sectionRepo;
    private final JPAQueryFactory queryFactory;

    public QueryBuilder sectionList() {
        QSection sectionTable = QSection.section;
        var query = queryFactory.select(sectionTable.id,
            sectionTable.name, sectionTable.description, sectionTable.createdAt,
            sectionTable.sort, sectionTable.status)
            .from(sectionTable);
        return new QueryBuilder(query);
    }

    public Integer findMaxSort() {
        QSection qSection = QSection.section;
        Integer sort = queryFactory.select(qSection.sort.max())
            .from(qSection)
            .fetchOne();
        return sort == null ? -1 : sort;
    }


    @Transactional
    public Long sectionCreate(String name, String description, Integer sort) {
        Section section = new Section();
        section.setCreatedAt(LocalDateTime.now());
        section.setName(name);
        section.setDescription(description);
        section.setSort(sort);
        Long id = sectionRepo.save(section).getId();
        sectionRepo.updateSort(id, findMaxSort() + 1);
        return id;
    }

    @Transactional
    public void sectionUpdate(Long id, String name, String description, Integer sort) {
        sectionRepo.update(id, name, description, sort);
    }

    public void sectionDelete(Long id) {
        sectionRepo.deleteById(id);
    }


}
