package win.pangniu.four.utils;

import win.pangniu.four.cache.redis.HwRedisCacheManager;
import win.pangniu.four.core.framework.SpringContextLoader;

import java.util.concurrent.TimeUnit;

/**
 * redis操作封装工具类
 */
public class RedisUtils {

    //private HashRedisTemplateMaker templateMaker = HashRedisTemplateMaker.getHashRedisTemplateMaker();

    private static HwRedisCacheManager hwRedisCacheManager = SpringContextLoader.getBean(HwRedisCacheManager.class);

    public static void set(String key, String value, long timeout, TimeUnit unit) {
        //templateMaker.getTemplate(key).opsForValue().set(key, value, timeout, unit);
        hwRedisCacheManager.getRedisTemplate(key).opsForValue().set(key, value, timeout, unit);
    }

    public static String get(String key) {
        //return templateMaker.getTemplate(key).opsForValue().get(key);
        return hwRedisCacheManager.getRedisTemplate(key).opsForValue().get(key);
    }

}
