package com.toy.artifact.controllers.admin;

import com.toy.artifact.db.entity.Admins;
import com.toy.artifact.db.service.AdminService;
import com.toy.artifact.db.vo.LoginRespVo;
import com.toy.artifact.utils.PasswordUtil;
import com.toy.artifact.utils.RespFormat.JsonError;
import com.toy.artifact.utils.RespFormat.JsonOk;
import com.toy.artifact.utils.RespFormat.JsonResp;
import com.toy.artifact.utils.jwt.JwtWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/admin")
@RestController
public class LoginController {

    public LoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    private final AdminService adminService;

    @RequestMapping("/login")
    public JsonResp<LoginRespVo> login(@RequestParam String username,
                                                 @RequestParam String password) {

        Admins admin = adminService.fetchLoginCredential(username);

        var passwordutil = new PasswordUtil();
        if (! passwordutil.verify(password, admin.getPassword())) {
            return new JsonError<>("用户名或密码不正确");
        }
        var vo = new LoginRespVo();
        vo.setToken(new JwtWrapper().sign(admin.getId()));
        vo.setUsername(username);
        return new JsonOk<>(vo);
    }

}
