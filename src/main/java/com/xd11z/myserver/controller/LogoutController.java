package com.xd11z.myserver.controller;

import com.xd11z.myserver.annotation.UserToken;
import com.xd11z.myserver.entity.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    //需要用户已登录
    @UserToken
    @GetMapping("/logout")
    public ServerResponse logout(){
        return ServerResponse.success("退出成功");
    }
}
