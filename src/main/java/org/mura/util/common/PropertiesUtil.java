package org.mura.util.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/31 20:37
 *
 * 获取配置信息
 */
public class PropertiesUtil {
    /**
     * properties文件转换为properties对象
     */
    private static final Properties PROPERTIES = new Properties();

    /**
     * 读取配置文件
     *
     * If the name begins with a '/' ('\u002f'),
     * then the absolute name of the resource is the portion of the name following the '/'.
     * Otherwise, the absolute name is of the following form:
     * modified_package_name/name
     * Where the modified_package_name is the package name of this object with '/' substituted
     * for '.' ('\u002e')
     */
    public static void readProperties(String fileName){
        InputStream inputStream = null;

        try {
            inputStream = PropertiesUtil.class.getResourceAsStream("/" + fileName);
            assert inputStream != null;
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            PROPERTIES.load(bf);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                if(inputStream != null){
                    inputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据key读取对应的value
     */
    public static String getProperty(String key){
        return PROPERTIES.getProperty(key);
    }
}
