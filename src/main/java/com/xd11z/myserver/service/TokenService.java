package com.xd11z.myserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xd11z.myserver.entity.User;
import org.springframework.stereotype.Service;

/**
 * 生成Token的服务类
 */
@Service
public class TokenService {
    public String getToken(User user)
    {
        String token = JWT.create().withAudience(user.getUserID())// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
