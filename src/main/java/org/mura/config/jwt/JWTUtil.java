package org.mura.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.mura.util.PropertiesUtil;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 21:20
 */
public class JWTUtil {
    /**
     * 校验token是否正确
     *
     * @param token  令牌
     * @param secret 秘钥
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
            verifier.verify(token);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 生成签名, EXPIRE_TIME后过期
     *
     * @param account 账号
     * @param secret 私钥
     * @return 加密的token
     */
    public static String sign(String account, String secret) {
//        获取过期时间，读取配置文件
        PropertiesUtil.readProperties("config.properties");
        String tokenExpireTime = PropertiesUtil.getProperty("tokenExpireTime");

//        此处单位是毫秒，所以要*1000
        Date date = new Date(System.currentTimeMillis() + Long.parseLong(tokenExpireTime) * 1000);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        // 附带account信息
        return JWT.create()
                .withClaim("account", account)
                .withExpiresAt(date)
                .sign(algorithm);
    }
}
