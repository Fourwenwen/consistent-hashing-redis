package win.pangniu.four.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import win.pangniu.four.core.service.PersonService;
import win.pangniu.four.utils.RedisUtils;

import java.util.concurrent.TimeUnit;

/**
 * 测试controller
 * 
 * @author fourwenwen
 *
 */
@RestController
public class IndexController {

	@Autowired
	private PersonService personService;

	@RequestMapping("user/{userid}")
	@ResponseBody
	public String user(@PathVariable("userid") String userid) {
		return personService.getPerson(userid).toString();
	}

	@RequestMapping("demo/{userid}")
	@ResponseBody
	public String demo(@PathVariable("userid") String userid) {
		return personService.getPerson2(userid).toString();
	}

	@RequestMapping("redis/{key}/{value}")
	@ResponseBody
	public String redis(@PathVariable("key") String key, @PathVariable("value") String value) {
        RedisUtils.set(key, value, 120, TimeUnit.SECONDS);
        return "ok";
	}

	@RequestMapping("redis/{key}")
	@ResponseBody
	public String readRedis(@PathVariable("key") String key) {
        return RedisUtils.get(key);
    }

}
