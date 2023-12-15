package com.xd11z.myserver.controller;
import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.annotation.PassToken;
import com.xd11z.myserver.entity.ServerResponse;
import com.xd11z.myserver.entity.User;
import com.xd11z.myserver.entity.UserInfo;
import com.xd11z.myserver.entity.UserLogin;
import com.xd11z.myserver.service.UserService;
import com.xd11z.myserver.util.TokenTool;
import com.xd11z.myserver.util.logger;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.xd11z.myserver.util.*;
import java.util.HashMap;
import java.util.Map;

@RestController

public class RegisterController {
    @Autowired
    UserService userService;
    @PostMapping(value = "/register")
    public ServerResponse userRegister(@RequestBody UserLogin userLogin, HttpServletResponse response) {
        String username = userLogin.username;
        String password = userLogin.password;

        if (!RegisterJDBC.isUserExists(username)) {
            // 插入用户并获取用户信息
            User newUser = RegisterJDBC.insertUser(username, password);

            if (newUser != null) {

                //在header附加上token，以后前端可以拿着这个来访问后端了（其实这里放在数据包里也可以）
                response.setHeader("Authorization", TokenTool.getToken(newUser));
                //将Authorization在响应首部暴露出来
                response.setHeader("Access-control-Expose-Headers", "Authorization");
                //成功可以new一个用户信息对象，然后存到服务器应答包中返回
                UserInfo userInfo = newUser.getInfo();
                return ServerResponse.success(userInfo);
            } else {
                // 插入失败，返回失败信息
                return ServerResponse.fail("注册失败！");
            }
        } else {
            return ServerResponse.fail("用户名已存在！");
        }
    }

}