package com.xd11z.myserver.controller;

import com.xd11z.myserver.annotation.UserToken;
import org.springframework.web.bind.annotation.*;

//端口在：src\main\resources\application.properties修改！！！

//测试类，用于验证后端已启动
@RestController
public class test {
    @GetMapping("/hello")
    public String run(){
        return "后端已启动.....";
    }

    @UserToken
    @GetMapping("/test")
    public String token(){
        return "登录成功";
    }

}

