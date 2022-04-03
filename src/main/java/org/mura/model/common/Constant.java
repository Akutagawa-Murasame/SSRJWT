package org.mura.model.common;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/2 22:47
 *
 * 常量
 */
public class Constant {
    /**
     * 常量1
     */
    public static final Integer INTEGER_1 = 1;

    /**
     * 2
     */
    public static final Integer INTEGER_2 = 2;

    /**
     * redis操作成功：OK
     */
    public static final String OK = "OK";

    /**
     * redis过期时间，以秒为单位，一分钟
     */
    public final static int EXRP_MINUTE = 60;

    /**
     * redis过期时间，以秒为单位，一小时
     */
    public final static int EXRP_HOUR = 60 * 60;

    /**
     * redis过期时间，以秒为单位，一天
     */
    public final static int EXRP_DAY = 60 * 60 * 24;

    /**
     * 缓存的key名称缓存前缀
     */
    public final static String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * 缓存的key名称访问前缀
     */
    public final static String PREFIX_SHIRO_ACCESS = "shiro:access_token:";

    /**
     * 缓存的key名称刷新前缀
     */
    public final static String PREFIX_SHIRO_REFRESH = "shiro:refresh_token:";
}
