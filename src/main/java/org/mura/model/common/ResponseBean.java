package org.mura.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 21:56
 *
 * 返回状态码bean对象
 */
@Data
@AllArgsConstructor
public class ResponseBean {
    /**
     * HTTP状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private Object data;
}