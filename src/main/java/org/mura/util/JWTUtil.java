package org.mura.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 21:20
 */
public class JWTUtil {
    /**
     * 过期时间5分钟
     */
    private static final long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 校验token是否正确
     *
     * @param token  令牌
     * @param secret 秘钥(这里秘钥都是取密码加Redis中保留的随机UUID)
     * @return 是否正确
     *
     * claim是jwt中的附加声明，即使不验证也没事，如果使用了withClaim就需要验证
     */
    public static boolean verify(String token, String secret) {
//        这个token使用密钥生成，不需要账户用户名，只需要私钥和token即可登录
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();

//            验证失败会报错，过期也会报错
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的头信息和payload无需secret解密也能获得，只有最后一部分签名拥有了服务端的密钥才能解密
     * token中规定了多少位是密钥，多少位是claim
     *
     * @param token 令牌
     * @return token中包含的账号
     */
    public static String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);

            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名, EXPIRE_TIME后过期
     *
     * @param account 账号
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String account, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带account信息
        return JWT.create()
                .withClaim("account", account)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
