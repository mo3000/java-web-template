package com.toy.artifact.db.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.artifact.db.composedkey.AdminRoleId;
import com.toy.artifact.db.entity.*;
import com.toy.artifact.db.repo.AdminRepo;
import com.toy.artifact.db.repo.AdminRoleRepo;
import com.toy.artifact.db.vo.AdminVo;
import com.toy.artifact.db.vo.RolesVo;
import com.toy.artifact.utils.Paginator;
import com.toy.artifact.utils.PasswordUtil;
import com.toy.artifact.utils.QueryBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {

    private final JPAQueryFactory queryFactory;
    private final AdminRepo adminRepo;
    private final AdminRoleRepo adminRoleRepo;

    public AdminService(JPAQueryFactory jpaQueryFactory,
                        AdminRepo adminRepo,
                        AdminRoleRepo adminRoleRepo) {
        this.queryFactory = jpaQueryFactory;
        this.adminRepo = adminRepo;

        this.adminRoleRepo = adminRoleRepo;
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

    public Admins fetchLoginCredential(String username) {
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

    @Transactional
    public Long create(String username, String realname, String password,
                       final List<Long> rolesid) {
        if (adminRepo.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }
        Admins admin = new Admins();
        admin.setUsername(username);
        admin.setStatus(0);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setPassword(new PasswordUtil().encode(password));
        admin.setRealname(realname);
        var newadmin = adminRepo.save(admin);
        List<AdminRole> adminRoleEntities = new ArrayList<>();
        rolesid.forEach(v -> {
            var adminRole = new AdminRole(newadmin.getId(), v);
            adminRoleEntities.add(adminRole);
        });
        adminRoleRepo.saveAll(adminRoleEntities);
        return newadmin.getId();
    }

    @Transactional
    public void updateRole(Long adminid, List<Long> roles) {
        QAdminRole qAdminRole = QAdminRole.adminRole;
        Set<Long> oldRoles = new HashSet<>(
            queryFactory.select(qAdminRole.roleid)
            .from(qAdminRole)
            .where(qAdminRole.adminid.eq(adminid))
            .fetch());
        Set<Long> delete = new HashSet<>(oldRoles);
        delete.removeAll(roles);
        adminRoleRepo.deleteAll(adminid, delete);
        Set<Long> add = new HashSet<>(roles);
        add.removeAll(oldRoles);
        List<AdminRole> saveData = new ArrayList<>();
        add.forEach(v -> saveData.add(new AdminRole(adminid, v)));
        adminRoleRepo.saveAll(saveData);
    }

    @Transactional
    public void update(Long id, String realname, List<Long> rolesid) {
        var admin = adminRepo.findById(id);
        if (admin.isEmpty()) {
            throw new RuntimeException("用户不存在, id: " + id);
        }
        admin.ifPresent(v -> {
            v.setRealname(realname);
            adminRepo.save(v);
            updateRole(id, rolesid);
        });
    }


    public AdminVo findById(Long id) {
        QAdmins admins = QAdmins.admins;
        QRoles rolesTable = QRoles.roles;
        var obj = queryFactory.select(admins.id, admins.username,
            admins.createdAt, admins.status)
            .where(admins.id.eq(id))
            .fetchOne();
        if (obj == null) {
            throw new UsernameNotFoundException("用户不存在, id: " + id);
        }
        var user = new AdminVo();
        user.setId(id);
        user.setStatus(obj.get(admins.status));
        user.setUsername(obj.get(admins.username));
        QAdminRole adminRole = QAdminRole.adminRole;
        List<RolesVo> roleList = new ArrayList<>();
        queryFactory.select(rolesTable.id, rolesTable.name)
            .from(rolesTable)
            .rightJoin(adminRole)
            .on(rolesTable.id.eq(adminRole.roleid))
            .where(adminRole.adminid.eq(id))
            .fetch()
            .forEach(v -> {
                RolesVo vo = new RolesVo(v.get(rolesTable.id), v.get(rolesTable.name));
                roleList.add(vo);
            });
        user.setRoles(roleList);
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
