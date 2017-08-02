package win.pangniu.four.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import win.pangniu.four.cache.redis.HwRedisCacheManager;
import win.pangniu.four.cache.redis.HwRedisConnectionFactory;
import win.pangniu.four.cache.redis.HwRedisTemplate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * cache的配置
 *
 * @author fourwenwen
 */
@Configuration
//@PropertySource(value = "classpath:/ch-cache.properties")
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    /**
     * key的生成器
     *
     * @return
     */
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {

            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }

        };
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return jedisPoolConfig;
    }

    // 启用redis哨兵模式
    /*@Bean
    public RedisSentinelConfiguration sentinelConfig() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
        sentinelConfig.setMaster("master");
        RedisNode redisNode = new RedisNode("127.0.0.1", 26379);
        sentinelConfig.addSentinel(redisNode);
        return sentinelConfig;
    }*/

    @Bean
    public HwRedisConnectionFactory factory(JedisPoolConfig jedisPoolConfig) {
        // 哨兵模式
        //HwRedisConnectionFactory factory = new HwRedisConnectionFactory(sentinelConfig(), jedisPoolConfig);
        // 非哨兵模式
        HwRedisConnectionFactory factory = new HwRedisConnectionFactory(jedisPoolConfig);
        return factory;
    }

    @Bean
    public HwRedisTemplate template(HwRedisConnectionFactory factory) {
        // 序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 实例化template
        HwRedisTemplate hwRedisTemplate = new HwRedisTemplate();
        hwRedisTemplate.setConnectionFactory(factory);
        hwRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        return hwRedisTemplate;
    }

    @Bean
    public CacheManager cacheManager(HwRedisTemplate template) {
        HwRedisCacheManager cacheManager = new HwRedisCacheManager(template);
        cacheManager.setDefaultExpiration(100);
        Map<String, Long> expires = new HashMap<String, Long>();
        expires.put("CACHE_HALF_A_MINUTE", 30L);
        expires.put("CACHE_ONE_MINUTE", 60L);
        expires.put("CACHE_QUARTER_HOUR", 900L);
        expires.put("CACHE_HALF_A_HOUR", 1800L);
        expires.put("CACHE_ONE_HOUR", 3600L);
        expires.put("CACHE_TWO_HOUR", 7200L);
        expires.put("CACHE_FOUR_HOUR", 14400L);
        expires.put("CACHE_EIGHT_HOUR", 28800L);
        expires.put("CACHE_HALF_A_DAY", 43200L);
        expires.put("CACHE_ONE_DAY", 86400L);
        cacheManager.setExpires(expires);
        return cacheManager;
    }

}
