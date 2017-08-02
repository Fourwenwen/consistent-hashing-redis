package win.pangniu.four.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取properties。
 * 打个广告好唔，更多这方面知识请看博客
 * http://blog.csdn.net/u014150463/article/details/51970593
 *
 * @author fourwenwen
 */
public class RedisProperties {

    private static Properties properties;

    static {
        properties = new Properties();
        String fileName = "ch-cache.properties";
        InputStream in = RedisProperties.class.getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 根据key，获取到相对应的Propertie。
     *
     * @param key
     * @return
     */
    public static Object getPropertie(String key) {
        return properties.get(key);
    }

}
