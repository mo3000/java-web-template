package com.toy.artifact.controllers.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.artifact.db.entity.*;
import com.toy.artifact.db.service.AdminService;
import com.toy.artifact.db.vo.AdminVo;
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



    @RequestMapping
    public List<Admins> index() {
        QAdmins admins = QAdmins.admins;
        List<Admins> result = queryFactory.selectFrom(admins)
            .orderBy(admins.createdAt.desc())
            .fetch();
        var mapper = new ObjectMapper();
        var str = result.stream()
            .map(v -> {
                try {
                    return mapper.writeValueAsString(v);
                } catch (JsonProcessingException e) {
                    return "";
                }
            })
            .collect(Collectors.joining("\n"));
        logger.info("beng da hei ya");

        return result;
    }

    @RequestMapping("/with-roles")
    public JsonResp<Paginator<AdminVo>> withRoles(@RequestParam(required = false) String wala,
                                                  @RequestParam(required = false) Long bigid,
                                                  @RequestParam(required = false) String[] names,
                                                  @RequestParam(required = false) String day,
                                                  @RequestParam(required = false, defaultValue = "1") Long page) {
        var query = adminService.admins()
            .paginate(page)
            .map(adminService::adminTupleToVo);
        return new JsonOk<>(query);
    }

    @RequestMapping("/edit")
    public JsonResp<Object> edit(@RequestParam String username,
                                   @RequestParam String realname,
                                   @RequestParam String password,
                                   @RequestParam(required = false) Long id,
                                   @RequestParam List<Long> roles) {

        try {
            if (id == null) {
                adminService.create(username, realname, password, roles);
            } else {
                adminService.update(id, realname, roles);
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
}
