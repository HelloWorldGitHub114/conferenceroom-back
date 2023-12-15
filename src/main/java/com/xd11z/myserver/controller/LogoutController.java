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
        //处理一些逻辑，比如把服务端存储的token删了?不过目前的代码里服务器没有存token，都是现场计算token对不对
        return ServerResponse.success("退出成功");
    }
}
