package win.pangniu.four.core.service;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import win.pangniu.four.core.entity.Person;

/**
 * 测试service
 * 
 * @author fourwenwen
 *
 */
@Service
public class PersonService {

	private final Logger log = Logger.getLogger(this.getClass());

	/**
	 * 自动生成key
	 * 
	 * @param id
	 * @return
	 */
	@Cacheable(value = "userCache", keyGenerator = "wiselyKeyGenerator")
	public Person getPerson(String id) {
		log.info("[userCacheManager]没有缓存，则执行下面内容。");
		Person person = new Person();
		person.setAge(10);
		person.setGender("男");
		person.setName(id);
		person.setId(id);
		return person;
	}

	/**
	 * 拿参数来当key
	 * 
	 * @param id
	 * @return
	 */
	@Cacheable(value = "demoCache", key = "#id")
	public Person getPerson2(String id) {
		log.info("[demoCacheManager]没有缓存，则执行下面内容。");
		Person person = new Person();
		person.setAge(10);
		person.setGender("男");
		person.setName(id);
		person.setId(id);
		return person;
	}

}
