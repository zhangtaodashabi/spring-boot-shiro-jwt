package com.yyy.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @program: hkpi
 * @description: 工具类
 * @author: Mr.Liu
 * @create: 2020-07-14 16:28
 **/
@Slf4j
public class JWTUtil {


    /* 过期时间*/
    private static final long EXPIRE_TIME = 60 * 24 * 60 * 1000L;
    // 密钥
    private static final String SECRET = "liu";
    // 生成token所需参数
    private static final String USERNAME = "username";
    private static final String ID = "id";

    private JWTUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 生成 token
     */
    public static String createToken(String username, Integer id) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            // 附带username信息
            return JWT.create()
                    .withClaim(USERNAME, username)
                    .withClaim(ID, id)
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
            return null;
        }

    }

    /**
     * 校验 token 是否正确
     */
    public static boolean verify(String token, String username, Integer id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(USERNAME, username)
                    .withClaim(ID, id)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            log.info(exception.getMessage());
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USERNAME).asString();
        } catch (JWTDecodeException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public static Integer getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(ID).asInt();
        } catch (JWTDecodeException e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
