package win.pangniu.four.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import win.pangniu.four.cache.redis.HwRedisCacheManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by fourwenwen on 2017/6/14.
 */
@Controller
public class IndexController {

    @Autowired
    private HwRedisCacheManager hwRedisCacheManager;

    @RequestMapping("redis/{key}/{value}")
    @ResponseBody
    public String redis(@PathVariable("key") String key, @PathVariable("value") String value) {
        hwRedisCacheManager.getRedisTemplate(key).opsForValue().set(key, value, 120, TimeUnit.SECONDS);
        return "ok";
    }

    @RequestMapping("redis/{key}")
    @ResponseBody
    public String readRedis(@PathVariable("key") String key) {
        return hwRedisCacheManager.getRedisTemplate(key).opsForValue().get(key);
    }
}
