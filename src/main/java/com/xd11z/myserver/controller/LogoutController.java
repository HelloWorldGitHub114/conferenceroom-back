package com.xd11z.myserver.controller;

import com.xd11z.myserver.annotation.UserLoginToken;
import com.xd11z.myserver.entity.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    //需要用户已登录
    @UserLoginToken
    @GetMapping("/logout")
    public ServerResponse logout(){
        //处理一些逻辑，比如把服务端存储的token删了?不过由于使用了jwt技术，并不用存储token，这里先保留着。
        return ServerResponse.success("退出成功");
    }
}
