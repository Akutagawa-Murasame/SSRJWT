package org.mura.util.convert;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/3 8:30
 */
public class JsonConvertUtil {
    /**
     * JSON to Entity
     */
    public static <T> T jsonToObject(String entityJson, Class<T> targetType) {
        return JSONObject.parseObject(entityJson, targetType);
    }

    /**
     * Entity to JSON
     */
    public static <T> String objectToJson(T target) {
        return JSONObject.toJSONString(target);
    }
}
