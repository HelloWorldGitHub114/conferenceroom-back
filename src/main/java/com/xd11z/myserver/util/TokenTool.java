package com.xd11z.myserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xd11z.myserver.entity.User;

/**
 * 生成Token的服务类
 * 1、令牌组成
 * - 标头（Header）
 * - 有效载荷（Payload）
 * - 签名（Signature）
 * - 因此，JWT通常如下所示: xxxx.yyyyy.zzzzz  Header.Payload.Signature
 *
 * 2、Header
 * - 标头通常由两部分组成：令牌的类型（即JWT）和所使用的签名算法，例如HMAC、SHA256或RSA。它会使用 Base64编码组成 JWT 结构的第一部分。
 * - 注意：Base64是一种编码，也就是说，它是可以被翻译回原来的样子的，它并不是一种加密的过程
 *
 * # 3、Payload
 * - 令牌的第二部分是有效负载，其中包含声明。声明是有关实体（通常是用户）和其他数据的声明。同样的,它会使用Base64 编码组成 JWT 结构的第二部分  不会放敏感信息
 *
 * # 4、Signature
 * - 前面两部分都是使用 Base64 进行编码的，即前端可以解开知道里面的信息。Signature 需要使用编码后的 header 和 payload 以及我们提供的一个密钥，然后使用 header 中指定的签名算法（HS256）进行签名。签名的作用是保证 JWT 没有被篡改过
 * - 如：
 * 	HMACSHA256(base64UrlEncode(header)+ "." + base64UrlEncode(payload),secret);
 * # 签名的目的
 * - 最后一步前面的过程，实际上是对头部以及负载内容进行签名，防止内容被篡改，如果有人对头部以及负载的内容解码之后进行修改，再进行编码，最后加上之前的签名组合形成新的JWT的话，那么服务器端会判断出新的头部和负载形成的签名和JWT附带上的签名是不一样的。如果要对新的头部和负载进行签名，在不知道服务器加密时用的密钥的话，得出来的签名也是不一样的。
 */

public class TokenTool {
    private static final String KEY = "HIFIMI_DAISIKI";
    public static String getToken(User user) {
        return JWT.create()
                .withClaim("userID",user.getUserID())// 将 user id 保存到 token 里面
                .withClaim("userName",user.getUsername())// 将 user name 保存到 token 里面
                .withClaim("userRole",user.getRole())// 将 user role 保存到 token 里面
                .sign(Algorithm.HMAC256(KEY));// 用KEY签名
    }

    /**
     * 验证token  合法性
     * - SignatureVerificationException   签名不一致异常
     * - TokenExpiredException            令牌过期异常
     * - AlgorirhmMismatchExceotion       算法不匹配异常
     * - InvalidClaimException            失效的payload异常
     */
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
    }
}

