package win.pangniu.four.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import win.pangniu.four.data.HashWheel;

import java.util.*;

/**
 * 实现CacheManager接口，并用hash环管理RedisCacheManager
 *
 * @author fourwenwen
 */
public class HwRedisCacheManager implements CacheManager {

    private final static Logger logger = LoggerFactory.getLogger(HwRedisCacheManager.class);

    private HashWheel<HashRedisCacheManager> hashWheel;
    /**
     * 超时时间配置项
     */
    private long defaultExpiration = 0;

    /**
     * redis协议配置项
     */
    private Map<String, Long> expires = null;

    public HwRedisCacheManager(HwRedisTemplate template) {
        List<RedisTemplate> redisTemplateList = template.getTemplates();
        List<HashRedisCacheManager> redisCacheManagerList = new ArrayList<HashRedisCacheManager>();
        for (RedisTemplate<String, String> redisTemplate : redisTemplateList) {
            HashRedisCacheManager cacheManager = new HashRedisCacheManager(redisTemplate);
            cacheManager.setDefaultExpiration(defaultExpiration); // 设置key-value超时时间
            cacheManager.setExpires(expires);
            redisCacheManagerList.add(cacheManager);
        }
        hashWheel = new HashWheel<HashRedisCacheManager>(redisCacheManagerList);
    }

    @Override
    public Cache getCache(String name) {
        return hashWheel.getHotspotsInfo(name).getCache(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        Set<String> cacheNames = Collections.emptySet();
        for (RedisCacheManager redisCacheManager : hashWheel.getHotspots()) {
            cacheNames.addAll(redisCacheManager.getCacheNames());
        }
        return cacheNames;
    }

    /**
     * 根据key来获取hash轮上面的热点
     *
     * @param key
     * @return
     */
    public RedisTemplate<String, String> getRedisTemplate(String key) {
        //RedisTemplate redisTemplate = hashWheel.getHotspotsInfo(key).getRedisTemplate();
        RedisTemplate redisTemplate = getValidRedisTemplate(key, 1);
        return redisTemplate;
    }

    /**
     * 根据key来获取hash轮上面的热点，获取有效的redisTemplate，避免因为某台机器宕机而导致系统不可用。
     *
     * @param key
     * @param num
     * @return
     */
    public RedisTemplate<String, String> getValidRedisTemplate(String key, int num) {
        RedisTemplate redisTemplate = hashWheel.getHotspotsInfoByNum(key, num).getRedisTemplate();
        try {
            redisTemplate.getConnectionFactory().getConnection();
        } catch (Exception e) {
            // TODO: 2017/6/21 这里可以加上预警功能，通知运维人员redis服务器出现异常。
            logger.error("redis出现故障，获取不了连接。警告！！！");
            return getValidRedisTemplate(key, num + 1);
        }
        return redisTemplate;
    }


    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public Map<String, Long> getExpires() {
        return expires;
    }

    public void setExpires(Map<String, Long> expires) {
        this.expires = expires;
    }

    /**
     * 保存redisTemplate对象
     */
    class HashRedisCacheManager extends RedisCacheManager {

        private RedisTemplate<String, String> redisTemplate;

        public HashRedisCacheManager(RedisTemplate<String, String> redisTemplate) {
            super(redisTemplate);
            this.redisTemplate = redisTemplate;
        }

        public RedisTemplate getRedisTemplate() {
            return redisTemplate;
        }
    }
}
