package com.dongsw.authority.conf;

import com.dongsw.authority.common.util.StringUtils;
import com.dongsw.authority.service.ResourceFiltersService;
import com.dongsw.authority.service.ResourceService;
import com.dongsw.authority.shiro.MShiroFilterFactoryBean;
import com.dongsw.authority.shiro.StatelessAuthcFilter;
import com.dongsw.authority.shiro.StatelessDefaultSubjectFactory;
import com.dongsw.authority.shiro.StatelessRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.io.Serializable;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    private static final Logger logger = LoggerFactory
            .getLogger(ShiroConfiguration.class);

    @Bean("statelessRealm")
    public StatelessRealm getStatelessRealm() {
        StatelessRealm statelessRealm = new StatelessRealm();
        statelessRealm.setCachingEnabled(false);
        return statelessRealm;
    }

    @Bean("subjectFactory")
    public StatelessDefaultSubjectFactory getStatelessDefaultSubjectFactory() {
        return new StatelessDefaultSubjectFactory();
    }

    @Bean()
    public RedisCacheManager getRedisCacheManager(String host, Integer port,
                                                  String pass) {
        RedisCacheManager rm = new RedisCacheManager();
        rm.setRedisManager(redisManager(host, port, pass));
        return rm;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String user,
            @Value("${spring.datasource.password}") String pass) {
        initFlyWay(url, user, pass);
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public SubjectDAO getSubjectDAO() {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        return subjectDAO;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(
            @Value("${spring.redis.host}") String host,
            @Value("${spring.redis.port}") Integer port,
            @Value("${spring.redis.password}") String pass) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(getStatelessRealm());
        dwsm.setSessionManager(sessionManager());
        dwsm.setSubjectFactory(getStatelessDefaultSubjectFactory());
        // 采用redis缓存
        dwsm.setCacheManager(getRedisCacheManager(host, port, pass));
        dwsm.setSubjectDAO(getSubjectDAO());
        // 注册manager,方便全局获取
        SecurityUtils.setSecurityManager(dwsm);
        return dwsm;
    }

    public DefaultSessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        // 不启用session
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        securityManager.setSessionManager(sessionManager());
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
     *
     * @param userService
     */
    private void loadShiroFilterChain(
            MShiroFilterFactoryBean shiroFilterFactoryBean,
            ResourceService resourceService) {
        logger.info("###########从数据库读取权限规则，加载到shiroFilter中##########");
        Map<String, String> filterChainDefinitionMap = resourceService
                .findResourcePermission();
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(
            DefaultWebSecurityManager securityManager,
            ResourceFiltersService filterService,
            RedisTemplate<Serializable, Object> template, Audience audience,
            ResourceService resourceService) {
        MShiroFilterFactoryBean shiroFilterFactoryBean = new MShiroFilterFactoryBean(
                filterService, template);
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/user/404");
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/user");
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/404");

        // 设置自定义filter
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();

        StatelessAuthcFilter rm = new StatelessAuthcFilter();
        rm.setAudience(audience);
        rm.setTemplate(template);
        rm.setResourceService(resourceService);
        filters.put("authc", rm);
        loadShiroFilterChain(shiroFilterFactoryBean, resourceService);
        return shiroFilterFactoryBean;
    }

    private void initFlyWay(String url, String user, String pass) {
        Flyway flyway = new Flyway();
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("/db/migration");
        flyway.setSqlMigrationPrefix("v");
        flyway.setSqlMigrationSuffix(".sql");
        flyway.setDataSource(url, user, pass);
        flyway.setCleanOnValidationError(true);
        flyway.migrate();
    }

    /**
     * 配置shiro redisManager
     *
     * @return
     */
    public RedisManager redisManager(String host, Integer port, String pass) {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(StringUtils.isBlank(pass) ? null : pass);
        redisManager.setExpire(1800);// 配置过期时间
        return redisManager;
    }
}