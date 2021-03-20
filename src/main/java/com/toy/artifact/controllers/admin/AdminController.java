package com.toy.artifact.controllers.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toy.artifact.db.entity.*;
import com.toy.artifact.db.service.AdminService;
import com.toy.artifact.db.vo.AdminVo;
import com.toy.artifact.utils.jwt.JwtWrapper;
import com.toy.artifact.utils.Paginator;
import com.toy.artifact.utils.RespFormat.JsonOk;
import com.toy.artifact.utils.RespFormat.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
