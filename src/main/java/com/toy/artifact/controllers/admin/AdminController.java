package com.toy.artifact.controllers.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.artifact.db.entity.*;
import com.toy.artifact.db.service.AdminService;
import com.toy.artifact.db.vo.AdminVo;
import com.toy.artifact.request.admin.admin.AdminEditRequest;
import com.toy.artifact.request.admin.admin.AdminListRequest;
import com.toy.artifact.request.admin.admin.ToggleStatusRequest;
import com.toy.artifact.utils.RespFormat.JsonError;
import com.toy.artifact.utils.jwt.JwtWrapper;
import com.toy.artifact.utils.Paginator;
import com.toy.artifact.utils.RespFormat.JsonOk;
import com.toy.artifact.utils.RespFormat.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.toy.artifact.utils.ExceptionWriter.dump;

import java.util.*;
import java.util.stream.Collectors;


@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;
    private final JPAQueryFactory queryFactory;
    private final Logger logger;
    private final JwtWrapper wrapper;

    public AdminController(AdminService adminService, JPAQueryFactory queryFactory,
                           JwtWrapper wrapper) {
        this.adminService = adminService;
        this.queryFactory = queryFactory;
        this.wrapper = wrapper;
        logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping("/test")
    public String test(@RequestParam Long userid,
                       @RequestParam(required = false) String name) {
//        String token = JWT.create()
//            .withIssuer("auth0")
//            .sign(Algorithm.HMAC256(Env));
        return wrapper.sign(userid);
//        JwtClaims claims = new JwtClaims();


//        return "yoyo " + new Random().nextInt(200);
    }


    @RequestMapping("/admin/edit")
    public JsonResp<Object> edit(@RequestBody AdminEditRequest request) {

        try {
            var roleids = adminService.rolenameToId(request.getRoles());
            if (request.getId() == null) {
                adminService.create(request.getUsername(), request.getRealname(), request.getPassword(), roleids);
            } else {
                adminService.update(request.getId(), request.getRealname(), roleids);
            }
        } catch (RuntimeException e) {
            logger.error(dump(e));
            return new JsonError<>(e.getMessage());
        }
        return new JsonOk<>(null);
    }

    @RequestMapping("/admin/list")
    public JsonResp<Paginator<AdminVo>> list(@RequestBody AdminListRequest req) {
        QAdmins qAdmins = QAdmins.admins;
        var list = adminService.admins()
            .when(req.getUsername() != null, query -> {
                query.where(qAdmins.username.like("%"+req.getUsername()+"%"));
            })
            .when(req.getRealname() != null, query -> {
                query.where(qAdmins.realname.like("%"+req.getRealname()+"%"));
            })
            .paginate(req.getPage(), req.getSize())
            .map(adminService::adminTupleToVo);
        Paginator<AdminVo> adminList = adminService.fillRoles(list);
        return new JsonOk<>(adminList);
    }

    @RequestMapping("/admin/toggle-status")
    public JsonResp<Object> toggleStatus(@RequestBody ToggleStatusRequest req) {
        adminService.toggleStatus(req.getId());
        return new JsonOk<>(null);
    }

    @RequestMapping("/admin/reset-password")
    public JsonResp<Object> resetPassword(@RequestBody ToggleStatusRequest req) {
        adminService.resetPassword(req.getId());
        return new JsonOk<>(null);
    }
}
