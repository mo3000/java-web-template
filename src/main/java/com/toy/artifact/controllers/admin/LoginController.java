package com.toy.artifact.controllers.admin;

import com.toy.artifact.db.entity.Admins;
import com.toy.artifact.db.service.AdminService;
import com.toy.artifact.db.vo.LoginRespVo;
import com.toy.artifact.utils.PasswordUtil;
import com.toy.artifact.utils.RespFormat.JsonError;
import com.toy.artifact.utils.RespFormat.JsonOk;
import com.toy.artifact.utils.RespFormat.JsonResp;
import com.toy.artifact.utils.jwt.JwtWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/admin")
@RestController
public class LoginController {

    public LoginController(AdminService adminService, JwtWrapper jwt) {
        this.adminService = adminService;
        this.jwt = jwt;
    }

    private final AdminService adminService;
    private final JwtWrapper jwt;

    @RequestMapping("/login")
    public JsonResp<LoginRespVo> login(@RequestBody Map<String, String> body) {
        Admins admin;
        try {
            admin = adminService.fetchLoginCredential(body.get("username"));
        } catch (RuntimeException e) {
            return new JsonError<>(e.getMessage());
        }
        Logger logger = LoggerFactory.getLogger(getClass());
        var passwordutil = new PasswordUtil();
        if (!passwordutil.verify(body.get("password"), admin.getPassword())) {
            return new JsonError<>("用户名或密码不正确");
        }
        var vo = new LoginRespVo();
        vo.setToken(jwt.sign(admin.getId()));
        vo.setUsername(body.get("username"));
        vo.setRoles(adminService.fetchAdminRoleName(admin.getId()));
        return new JsonOk<>(vo);
    }

}
