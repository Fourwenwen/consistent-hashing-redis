package win.pangniu.four;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import win.pangniu.four.core.framework.SpringContextLoader;

/**
 * 启动类
 * 
 * @author fourwenwen
 *
 */
@SpringBootApplication
@ComponentScan
@EnableTransactionManagement
public class App {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(App.class);
		// 把上下文全局化
		SpringContextLoader.init(springApplication.run(args));
	}
}
