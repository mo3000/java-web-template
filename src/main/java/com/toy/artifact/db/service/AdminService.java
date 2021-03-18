package com.toy.artifact.db.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.artifact.db.entity.Admins;
import com.toy.artifact.db.entity.QAdminRole;
import com.toy.artifact.db.entity.QAdmins;
import com.toy.artifact.db.entity.QRoles;
import com.toy.artifact.db.vo.AdminVo;
import com.toy.artifact.db.vo.RolesVo;
import com.toy.artifact.utils.Paginator;
import com.toy.artifact.utils.QueryBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final JPAQueryFactory queryFactory;

    public AdminService(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    public AdminVo adminTupleToVo(Tuple v) {
        QAdmins admins = QAdmins.admins;
        var vo = new AdminVo();
        vo.setRealname(v.get(admins.realname));
        vo.setId(v.get(admins.id));
        vo.setCreated_at(v.get(admins.createdAt));
        vo.setStatus(v.get(admins.status));
        vo.setUsername(v.get(admins.username));
        return vo;
    }

    public Admins findByUsername(String username) {
        QAdmins admins = QAdmins.admins;
        var obj = queryFactory.select(admins.id, admins.username, admins.password,
         admins.createdAt, admins.status)
            .where(admins.username.eq(username))
            .fetchOne();
        if (obj == null) {
            throw new UsernameNotFoundException(String.format("用户名: %s 不存在", username));
        }
        var user = new Admins();
        user.setId(obj.get(admins.id));
        user.setPassword(obj.get(admins.password));
        user.setStatus(obj.get(admins.status));
        user.setCreatedAt(obj.get(admins.createdAt));
        user.setUsername(username);
        return user;
    }

    public QueryBuilder admins() {
        QAdmins admins = QAdmins.admins;
        return new QueryBuilder(
            queryFactory.select(admins.id, admins.username, admins.createdAt,
                admins.realname, admins.status)
                .from(admins));
    }

    public JPAQuery<Tuple> queryWithRoles(JPAQuery<Tuple> adminQuery) {
        QAdmins adminsTable = QAdmins.admins;
        QRoles rolesTable = QRoles.roles;
        QAdminRole adminRole = QAdminRole.adminRole;
        return adminQuery
            .leftJoin(adminRole)
            .on(adminsTable.id.eq(adminRole.adminid))
            .leftJoin(rolesTable)
            .on(adminRole.roleid.eq(rolesTable.id));
    }

    public Paginator<AdminVo> fillRoles(final Paginator<AdminVo> admins) {
        QAdmins adminsTable = QAdmins.admins;
        QRoles rolesTable = QRoles.roles;

        return admins.fill(keys -> {
            Map<Long, List<RolesVo>> rolesMap = new HashMap<>();
            var query = queryFactory.select(rolesTable.id, rolesTable.name, adminsTable.id)
                .from(adminsTable);
            queryWithRoles(query)
                .where(adminsTable.id.in(keys))
                .fetch()
                .forEach(r -> {
                    var id = r.get(adminsTable.id);
                    if (rolesMap.containsKey(id)) {
                        rolesMap.get(id).add(new RolesVo(
                            r.get(rolesTable.id), r.get(rolesTable.name)));
                    } else {
                        List<RolesVo> list = new ArrayList<>();
                        list.add(new RolesVo(
                            r.get(rolesTable.id), r.get(rolesTable.name)));
                        rolesMap.put(id, list);
                    }
                });
            admins.forEach(v -> {
                v.setRoles(rolesMap.get(v.getId()));
            });
            return admins;
        });
    }


}
