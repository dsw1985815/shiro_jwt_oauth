package com.dongsw.authority.conf;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.dongsw.authority.common.model.CustomRestTemplate;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * Created by 戚羿辰 on 2017/02/11。
 */
@Configuration
public class BeanConfig {

	private final Logger logger = LoggerFactory.getLogger("【yaml文件的配置读取】");

	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RedisTemplate<Serializable, Object> pubRedis(RedisConnectionFactory factory) {
		RedisTemplate<Serializable, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public Properties yaml() {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		Properties resultProperties = null;
		try
		{
			logger.info("从<配置文件>中<>读取");
			yaml.setResources(resolver.getResources("classpath*:/**/*.yml"));
			resultProperties = yaml.getObject();
		} catch (Exception e)
		{
			logger.error("无法读取 yaml 文件", e);
			resultProperties = null;
		}
		return resultProperties;
	}

	@Bean("pathMap")
	public Map<String, String> pathMap() {
		Map<String, String> pathMap = new HashMap<>();
		for (Entry<Object, Object> set : yaml().entrySet())
		{
			pathMap.put(String.valueOf(set.getKey()), String.valueOf(set.getValue()));
		}
		return pathMap;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public CustomRestTemplate restTemplate() {
		CustomRestTemplate restTemplate = new CustomRestTemplate();
		// delete方式的requestbody支持
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory() {
			@Override
			protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
				if (HttpMethod.DELETE == httpMethod)
				{
					return new HttpEntityEnclosingDeleteRequest(uri);
				}
				return super.createHttpUriRequest(httpMethod, uri);
			}

			class HttpEntityEnclosingDeleteRequest extends HttpEntityEnclosingRequestBase {
				public HttpEntityEnclosingDeleteRequest(final URI uri)
				{
					super();
					setURI(uri);
				}

				@Override
				public String getMethod() {
					return "DELETE";
				}
			}
		});
		return restTemplate;
	}

}
