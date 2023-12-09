package com.xd11z.myserver.controller;
import com.xd11z.myserver.entity.*;
import com.xd11z.myserver.service.UserService;
import com.xd11z.myserver.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

public class RegisterController {
    @Autowired
    UserService userService;
    @PostMapping(value = "/register")
    public ServerResponse userRegister(@RequestBody Map<String, String> request){
        String username = request.get("username");
        String password = request.get("password");
        if(!RegisterJDBC.isUserExists(username)){
            RegisterJDBC.insertUser(username,password);
            return ServerResponse.success("注册成功");
        }
        else{
            return ServerResponse.fail("用户名已存在！");
        }
    }
}
