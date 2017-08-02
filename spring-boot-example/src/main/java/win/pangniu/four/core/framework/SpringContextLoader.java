package win.pangniu.four.core.framework;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * 把上下文全局化，并暴露出去
 * @author fourwenwen
 *
 */
public abstract  class SpringContextLoader {

	public static ConfigurableApplicationContext configurableApplicationContext = null;
	
	public static void init(ConfigurableApplicationContext configurableApplicationContext){
		SpringContextLoader.configurableApplicationContext = configurableApplicationContext;
	}
	
	public static Object getBean(String beanName){
		return configurableApplicationContext.getBean(beanName);
	}
	
	public static <T> T getBean(String beanName, Class<T> requiredType){
		return configurableApplicationContext.getBean(beanName, requiredType);
	}

	public static <T> T getBean(Class<T> beanClass) {
		return configurableApplicationContext.getBean(beanClass);
	}
	
}
