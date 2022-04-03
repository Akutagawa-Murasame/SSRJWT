package org.mura.util.convert;

import org.mura.model.common.Constant;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/31 20:29
 *
 * 十六进制转换工具
 */
public class HexConvertUtil {
    /**
     * 将二进制转换成16进制
     */
    public static String parseByteToHexStr(byte[] buff) {
        StringBuilder sb = new StringBuilder();

        for (byte b : buff) {
//            为什么要&0xff：https://www.cnblogs.com/think-in-java/p/5527389.html
            String hex = Integer.toHexString(b & 0xFF);

            if (hex.length() == Constant.INTEGER_1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStrToByte(String hexStr) {
        if (hexStr.length() < Constant.INTEGER_1){
            return null;
        }

        byte[] result = new byte[hexStr.length() / Constant.INTEGER_2];
        for (int i = 0, len = hexStr.length() / Constant.INTEGER_2; i < len; ++i) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);

            result[i] = (byte) (high * 16 + low);
        }

        return result;
    }
}
