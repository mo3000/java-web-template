package com.toy.artifact.controllers.admin;

import com.toy.artifact.db.vo.LoginRespVo;
import com.toy.artifact.utils.RespFormat.JsonOk;
import com.toy.artifact.utils.RespFormat.JsonResp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class LoginController {

    @RequestMapping("/login")
    public JsonResp<LoginRespVo> login(@RequestParam String username,
                                       @RequestParam String password) {
        var vo = new LoginRespVo();
        var token = new BCryptPasswordEncoder(13).encode(password);
        vo.setToken(token);
        vo.setUsername(username);
        return new JsonOk<>(vo);
    }

}
