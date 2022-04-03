package org.mura.config.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/29 13:11
 */
public class JWTToken implements AuthenticationToken {
    private static final long serialVersionUID = 8365707295089463707L;
    /**
     * token
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
