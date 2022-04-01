package org.mura.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/31 20:27
 *
 * Base64对称加密算法
 */
public class EncryptBase64Util {
    /**
     * 加密
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));

        return new String(encodeBytes);
    }

    /**
     * 解密
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));

        return new String(decodeBytes);
    }
}
