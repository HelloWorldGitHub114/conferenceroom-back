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
    public ServerResponse userRegister(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (!RegisterJDBC.isUserExists(username)) {
            // 插入用户并获取用户信息
            User newUser = RegisterJDBC.insertUser(username, password);

            if (newUser != null) {
                // 生成token
                String token = TokenTool.getToken(newUser);
                // 创建包含用户信息和令牌的响应
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("username", newUser.getUsername());
                responseData.put("role", newUser.getRole());
                responseData.put("userID", newUser.getUserID());
                return ServerResponse.success(responseData);
            } else {
                // 插入失败，返回失败信息
                return ServerResponse.fail("注册失败！");
            }
        } else {
            return ServerResponse.fail("用户名已存在！");
        }
    }

}
