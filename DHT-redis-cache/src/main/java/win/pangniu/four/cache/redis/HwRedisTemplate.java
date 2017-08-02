package win.pangniu.four.cache.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * 一致性hash的redis模版
 * Created by fourwenwen on 2017/6/7.
 */
public class HwRedisTemplate implements InitializingBean {

    private HwRedisConnectionFactory connectionFactory;

    private List<RedisTemplate> templates;
    private RedisSerializer keySerializer = null;
    private RedisSerializer valueSerializer = null;
    private RedisSerializer hashKeySerializer = null;
    private RedisSerializer hashValueSerializer = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        templates = new ArrayList<RedisTemplate>();
        List<JedisConnectionFactory> factorys = connectionFactory.getFactories();
        for (JedisConnectionFactory factory : factorys) {
            StringRedisTemplate template = new StringRedisTemplate(factory);
            template.setKeySerializer(keySerializer);
            template.setValueSerializer(valueSerializer);
            template.setHashKeySerializer(hashKeySerializer);
            template.setHashValueSerializer(hashValueSerializer);
            // 执行必要方法
            template.afterPropertiesSet();
            templates.add(template);
        }
    }

    public List<RedisTemplate> getTemplates() {
        return templates;
    }

    public RedisSerializer getKeySerializer() {

        return keySerializer;
    }

    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public RedisSerializer getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public RedisSerializer getHashKeySerializer() {
        return hashKeySerializer;
    }

    public void setHashKeySerializer(RedisSerializer hashKeySerializer) {
        this.hashKeySerializer = hashKeySerializer;
    }

    public RedisSerializer getHashValueSerializer() {
        return hashValueSerializer;
    }

    public void setHashValueSerializer(RedisSerializer hashValueSerializer) {
        this.hashValueSerializer = hashValueSerializer;
    }

    public HwRedisConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(HwRedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
