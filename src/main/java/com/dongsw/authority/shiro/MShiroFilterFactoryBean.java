package com.dongsw.authority.shiro;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.model.ResourceFilters;
import com.dongsw.authority.service.ResourceFiltersService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.data.redis.core.RedisTemplate;

public class MShiroFilterFactoryBean extends ShiroFilterFactoryBean {

	// 对ShiroFilter来说，需要直接忽略的请求
	

	// 将filterMap放入redis,以支持热部署和集群
	private RedisTemplate<Serializable, Object>	template;

	private ResourceFiltersService resourceFiltersService;


	public MShiroFilterFactoryBean(ResourceFiltersService filterService, RedisTemplate<Serializable, Object> template)
	{
		super();
		Set<String>							ignoreExt = new HashSet<>();
		this.template = template;
		template.opsForValue().set(ConstantDef.RESOURCE_DEFAULT_URL_LIST, new HashMap<>());
		this.resourceFiltersService = filterService;
		List<ResourceFilters> list = resourceFiltersService.all();
		for (ResourceFilters resource : list)
		{
			ignoreExt.add(resource.getFilterType());
		}
		template.opsForValue().set(ConstantDef.RESOURCE_FILTER_SET, ignoreExt);
	}


	public MShiroFilterFactoryBean()
	{
        template.opsForValue().set(ConstantDef.RESOURCE_DEFAULT_URL_LIST, new HashMap<>());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getFilterChainDefinitionMap() {
		return (Map<String, String>) template.opsForValue().get(ConstantDef.RESOURCE_DEFAULT_URL_LIST);
	}

	/**
	 * Sets the chainName-to-chainDefinition map of chain definitions to use for creating filter chains intercepted
	 * by the Shiro Filter.  Each map entry should conform to the format defined by the
	 * {@link FilterChainManager#createChain(String, String)} JavaDoc, where the map key is the chain name (e.g. URL
	 * path expression) and the map value is the comma-delimited string chain definition.
	 *
	 * @param filterChainDefinitionMap the chainName-to-chainDefinition map of chain definitions to use for creating
	 *                                 filter chains intercepted by the Shiro Filter.
	 */
	@Override
	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		template.opsForValue().set(ConstantDef.RESOURCE_DEFAULT_URL_LIST, filterChainDefinitionMap);
	}


	@Override
	protected AbstractShiroFilter createInstance() throws Exception {

		SecurityManager securityManager = getSecurityManager();
		if (securityManager == null)
		{
			String msg = "SecurityManager property must be set.";
			throw new BeanInitializationException(msg);
		}

		if (!(securityManager instanceof WebSecurityManager))
		{
			String msg = "The security manager does not implement the WebSecurityManager interface.";
			throw new BeanInitializationException(msg);
		}

		FilterChainManager manager = createFilterChainManager();
		PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(manager);
		return new MSpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
	}

	private final class MSpringShiroFilter extends AbstractShiroFilter {

		protected MSpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver)
		{
			super();
			if (webSecurityManager == null)
			{
				throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
			}
			setSecurityManager(webSecurityManager);
			if (resolver != null)
			{
				setFilterChainResolver(resolver);
			}
		}

		@Override
		protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
				FilterChain chain) throws ServletException, IOException {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String str = request.getRequestURI().toLowerCase();
			boolean flag = true;
			int idx;
			SecurityUtils.getSubject().getSession(false);
			if ((idx = str.lastIndexOf('.')) > 0)
			{
				str = str.substring(idx);
				if (getIgnoreExt().contains(str.toLowerCase()))
					flag = false;
			}
			if (flag)
			{
				super.doFilterInternal(servletRequest, servletResponse, chain);
			} else
			{
				chain.doFilter(servletRequest, servletResponse);
			}
 		}

		@SuppressWarnings("unchecked")
		private Set<String>	getIgnoreExt(){
			return (Set<String>) template.opsForValue().get(ConstantDef.RESOURCE_FILTER_SET);
		}

	}



}