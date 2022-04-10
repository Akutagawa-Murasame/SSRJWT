package org.mura.util.common;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/3 10:36
 */
public class StringUtil {
    /**
     * 下划线
     */
    private static final char UNDERLINE = '_';

    /**
     * String为空判断(不允许空格)
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * String不为空判断(不允许空格)
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Byte数组为空判断
     */
    public static boolean isNull(byte[] bytes){
        // 根据byte数组长度为0判断
        return bytes == null || bytes.length == 0;
    }

    /**
     * Byte数组不为空判断
     */
    public static boolean isNotNull(byte[] bytes) {
        return !isNull(bytes);
    }

    /**
     * 驼峰转下划线工具
     */
    public static String camelToUnderline(String param) {
        if (isNotBlank(param)) {
            int len = param.length();
            StringBuilder stringBuilder = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);

                if (Character.isUpperCase(c)) {
                    stringBuilder.append(UNDERLINE);
                    stringBuilder.append(Character.toLowerCase(c));
                } else {
                    stringBuilder.append(c);
                }
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * 下划线转驼峰工具
     */
    public static String underlineToCamel(String param) {
        if (isNotBlank(param)) {
            int len = param.length();
            StringBuilder stringBuilder = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);

//                '_'的ASCII码是95
                if (c == 95) {
                    ++i;
                    if (i < len) {
                        stringBuilder.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    stringBuilder.append(c);
                }
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * 在字符串两侧添加单引号
     */
    public static String addSingleQuotes(String param) {
        if (param == null) {
            param = "";
        }

        return "'" + param + "'";
    }
}
