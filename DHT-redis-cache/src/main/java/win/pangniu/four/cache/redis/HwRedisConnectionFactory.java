package win.pangniu.four.cache.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;
import win.pangniu.four.common.RedisProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * hash轮的redis连接工厂
 * Created by fourwenwen on 2017/6/7.
 */
public class HwRedisConnectionFactory implements InitializingBean {

    private List<JedisConnectionFactory> factories;

    private JedisPoolConfig poolConfig = new JedisPoolConfig();

    private RedisSentinelConfiguration sentinelConfig;

    public HwRedisConnectionFactory() {
    }

    public HwRedisConnectionFactory(JedisPoolConfig poolConfig) {
        this(null, poolConfig);
    }

    public HwRedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
        this.sentinelConfig = sentinelConfig;
        this.poolConfig = poolConfig != null ? poolConfig : new JedisPoolConfig();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        int redisCount = Integer.parseInt(RedisProperties.getPropertie("redis.count").toString());
        factories = new ArrayList<JedisConnectionFactory>(redisCount);
        for (int i = 0; i < redisCount; i++) {
            int dbSize = 1;
            Object dbSizeObj = RedisProperties.getPropertie("redis.db.size" + i);
            if (dbSizeObj != null) {
                dbSize = Integer.valueOf(dbSizeObj.toString());
            }
            for (int j = 0; j < dbSize; j++) {
                JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfig,poolConfig);
                factory.setHostName(RedisProperties.getPropertie("redis.host" + i).toString());
                factory.setPort(Integer.parseInt(RedisProperties.getPropertie("redis.port" + i).toString()));
                factory.setPassword(RedisProperties.getPropertie("reids.passwd" + i).toString());
                factory.setTimeout(Integer.parseInt(RedisProperties.getPropertie("redis.timeout").toString()));
                factory.setDatabase(j);
                // 执行必要方法
                factory.afterPropertiesSet();
                factories.add(factory);
            }
        }
    }

    public List<JedisConnectionFactory> getFactories() {
        return factories;
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public RedisSentinelConfiguration getSentinelConfig() {
        return sentinelConfig;
    }

    public void setSentinelConfig(RedisSentinelConfiguration sentinelConfig) {
        this.sentinelConfig = sentinelConfig;
    }
}
