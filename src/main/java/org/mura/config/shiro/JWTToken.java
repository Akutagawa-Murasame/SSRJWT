package org.mura.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/29 13:11
 */
public class JWTToken implements AuthenticationToken {
    /**
     * 密钥
     */
    private final String token;

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
}
