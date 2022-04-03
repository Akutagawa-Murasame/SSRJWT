package org.mura.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 17:20
 */
@Slf4j
public class JsonListUtil {
    /**
     * List to json
     */
    public static <T> String listToJson(List<T> targets) {
        return JSON.toJSONString(targets);
    }

    /**
     * json 转 List
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        return JSONArray.parseArray(jsonString, clazz);
    }
}
