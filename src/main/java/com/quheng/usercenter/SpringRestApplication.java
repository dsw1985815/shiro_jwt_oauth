package com.quheng.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quheng.usercenter.filter.HTTPBasicAuthorizeAttribute;
import com.quheng.usercenter.filter.HTTPBearerAuthorizeAttribute;
import com.quheng.usercenter.config.Audience;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;


@SpringBootApplication
@EnableConfigurationProperties(Audience.class)
public class SpringRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestApplication.class, args);
	}

	/*
	@Bean
	public FilterRegistrationBean  basicFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute();
		registrationBean.setFilter(httpBasicFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/user/getuser");
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}
	*/
	@Bean
	public FilterRegistrationBean jwtFilterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		HTTPBearerAuthorizeAttribute httpBearerFilter = new HTTPBearerAuthorizeAttribute();
		registrationBean.setFilter(httpBearerFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/user/getusers");
		urlPatterns.add("/user/getuser");
		urlPatterns.add("/user/detail");
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}


	@Bean
	public VelocityEngineFactoryBean velocityEngineFactoryBean(){
		VelocityEngineFactoryBean velocityEngineFactoryBean = new VelocityEngineFactoryBean();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("input.encoding","UTF-8");
		map.put("output.encoding","UTF-8");
		map.put("resource.loader","class");
		map.put("class.resource.loader.public.name","String Template");
		map.put("class.resource.loader.description","Velocity String Template Resource Loader");
		map.put("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		map.put("class.resource.loader.cache","false");
		map.put("class.resource.loader.modificationCheckInterval","60");
		map.put("velocimacro.library","");
		velocityEngineFactoryBean.setVelocityPropertiesMap(map);
		return velocityEngineFactoryBean;
	}

}