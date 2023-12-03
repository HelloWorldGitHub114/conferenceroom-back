package com.xd11z.myserver.controller;

import com.alibaba.fastjson.JSON;
import com.xd11z.myserver.entity.ServerResponse;
import com.xd11z.myserver.entity.User;
import com.xd11z.myserver.entity.UserInfo;
import com.xd11z.myserver.entity.UserLogin;
import com.xd11z.myserver.util.TokenTool;
import com.xd11z.myserver.util.UserTool;
import com.xd11z.myserver.util.logger;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//登录类
@RestController
public class LoginController {
    /**
     * 处理login的请求的函数
     * @param userLogin 前端传入的登录信息，包括username和password
     * @return 服务器应答类
     */
    @PostMapping(value = "/login")
    public ServerResponse loginIn(@RequestBody UserLogin userLogin, HttpServletResponse response) {
        //记录到日志文件中，可以不写
        logger.write("./src/main/java/com/xd11z/myserver/tool/log.txt", JSON.toJSONString(userLogin));
        //判断userLogin是否正确
        String msg = null;
        User user = UserTool.CheckUserLogin(userLogin,msg);
        if(user.userID=="0" || user.userID=="1")
        {
            //在header附加上token，以后前端可以拿着这个来访问后端了（其实这里放在数据包里也可以）
            response.setHeader("Authorization", TokenTool.getToken(user));
            //成功可以new一个用户信息对象，然后存到服务器应答包中返回
            UserInfo userInfo = user.getInfo();
            return ServerResponse.success(userInfo);
        }
        else
        {
            if(user.userID=="2") return ServerResponse.fail("用户名不存在");
            else if(user.userID=="3") return ServerResponse.fail("用户名与密码不匹配");
            else return ServerResponse.fail("Unkown Error!");
        }
    }
}
