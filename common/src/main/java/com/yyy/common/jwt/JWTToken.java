package com.yyy.common.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @program: hkpi
 * @description: token
 * @author: Mr.Liu
 * @create: 2020-07-14 16:28
 **/

public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }
}
