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

//登录类
@RestController
public class LoginController {
    @Autowired
    UserService userService;

    /**
     * 处理login的请求的函数
     * @param userLogin 前端传入的登录信息，包括username和password
     * @return 服务器应答类
     */
    @PassToken
    @PostMapping(value = "/login")
    public ServerResponse loginIn(@RequestBody UserLogin userLogin, HttpServletResponse response) {
        //记录到日志文件中，可以不写
        logger.write("./src/main/java/com/xd11z/myserver/util/log.txt", JSON.toJSONString(userLogin));
        StringBuilder msg = new StringBuilder("");
        //判断userLogin是否正确
        User user = userService.CheckUserLogin(userLogin,msg);
        if(user!=null)
        {
            //在header附加上token，以后前端可以拿着这个来访问后端了
            response.setHeader("Authorization", TokenTool.getToken(user));
            //将Authorization在响应首部暴露出来
            response.setHeader("Access-control-Expose-Headers", "Authorization");
            //成功可以new一个用户信息对象，然后存到服务器应答包中返回
            UserInfo userInfo = user.getInfo();
            return ServerResponse.success(userInfo);
        }
        else return ServerResponse.fail(msg.toString());
    }
}
