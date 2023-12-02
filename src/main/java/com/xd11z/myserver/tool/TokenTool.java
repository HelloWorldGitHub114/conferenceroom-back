package com.xd11z.myserver.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xd11z.myserver.data.User;

/**
 * 生成Token的服务类
 */

public class TokenTool {
    public static String getToken(User user) {
        return JWT.create().withAudience(user.getUserID())// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));
    }
}

