package com.toy.artifact.utils;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.List;

public class QueryBuilder {

    private final JPAQuery<Tuple> query;

    public QueryBuilder(JPAQuery<Tuple> query) {
        this.query = query;
    }

    public QueryBuilder where(QueryCondition func) {
        func.use(query);
        return this;
    }

    public QueryBuilder when(boolean cond, QueryCondition func) {
        if (cond) {
            func.use(query);
        }
        return this;
    }

    public static interface QueryCondition {
        public void use(JPAQuery<Tuple> query);
    }

    public List<Tuple> get() {
        return query.fetch();
    }

    public Paginator<Tuple> paginate(long page, int pageSize) {
        long total = query.fetchCount();
        List<Tuple> data = query.limit(pageSize)
            .offset(page * pageSize)
            .fetch();
        return new Paginator<>(data, total, page, pageSize);
    }

    public Paginator<Tuple> paginate(long page) {
        return paginate(page, 15);
    }

}
