package org.mura.util.encrypt;

import com.sun.crypto.provider.SunJCE;
import lombok.extern.slf4j.Slf4j;
import org.mura.exception.UnauthorizedException;
import org.mura.util.convert.HexConvertUtil;
import org.mura.util.PropertiesUtil;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Objects;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/31 20:11
 *
 * AES对称加密算法
 */
@Slf4j
public class EncryptAESUtil {
    /**
     * 读取配置，获取私钥，生成对称密钥
     */
    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException {
        Security.addProvider(new SunJCE());

        // 实例化支持AES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
        // KeyGenerator 提供对称密钥生成器的功能，支持各种算法
        KeyGenerator keygen = KeyGenerator.getInstance("AES");

        // 读取配置文件，获取私钥
        PropertiesUtil.readProperties("config.properties");

        String key = EncryptBase64Util.decode(PropertiesUtil.getProperty("encryptAESKey"));

        // 将key进行转换为byte[]数组
        keygen.init(128, new SecureRandom(key.getBytes()));

        // SecretKey 负责保存对称密钥 生成密钥
        return keygen.generateKey();
    }

    /**
     * 加密
     */
    public static String encrypt(String str) {
        try{
            SecretKey desKey = generateSecretKey();

            // 生成Cipher对象,指定其支持的AES算法，Cipher负责完成加密或解密工作
            Cipher cipher = Cipher.getInstance("AES");

            // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式，doFinal由此判断是加密还是解密
            cipher.init(Cipher.ENCRYPT_MODE, desKey);

            // 该字节数组负责保存加密的结果
            byte[] cipherByte = cipher.doFinal(str.getBytes());

            // 先将二进制转换成16进制，再返回Base64加密后的String
            return EncryptBase64Util.encode(HexConvertUtil.parseByteToHexStr(cipherByte));
        } catch (NoSuchAlgorithmException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidKeyException |
                NoSuchPaddingException |
                UnsupportedEncodingException e){
            log.error(e.getMessage());

            throw new UnauthorizedException("encrypt exception\n" + e.getMessage());
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String str) {
        try{
            SecretKey desKey = generateSecretKey();

            Cipher cipher = Cipher.getInstance("AES");

            // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示解密模式
            cipher.init(Cipher.DECRYPT_MODE, desKey);

            // 该字节数组负责保存加密的结果，先对str进行Base64解密，将16进制转换为二进制
            byte[] cipherByte = cipher.doFinal(
                    Objects.requireNonNull(
                            HexConvertUtil.parseHexStrToByte(EncryptBase64Util.decode(str))
                    ));

            return new String(cipherByte);
        } catch (NoSuchAlgorithmException |
                BadPaddingException |
                IllegalBlockSizeException |
                InvalidKeyException |
                UnsupportedEncodingException |
                NoSuchPaddingException e){
            log.error(e.getMessage());

            throw new UnauthorizedException("decrypt exception\n" + e.getMessage());
        }
    }
}