package com.dongsw.authority;

import com.dongsw.authority.conf.Audience;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement	//启动事务
@EnableConfigurationProperties(Audience.class)
public class AuthorityApplication {
	protected AuthorityApplication() {
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(AuthorityApplication.class);
		application.run(args); // 启动
	}
}
