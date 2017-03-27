package com.dongsw.authority.shiro;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.util.JwtHelper;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.service.ResourceService;

import io.jsonwebtoken.Claims;

@SuppressWarnings("unchecked")
public class StatelessAuthcFilter extends AccessControlFilter {

    private static final Logger logger = LoggerFactory.getLogger(StatelessAuthcFilter.class);


    RedisTemplate<Serializable, Object> template;

    ResourceService resourceService;

    private Audience audience;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String auth = getAuth(req);
        Claims claims = JwtHelper.parseJWT(auth, audience.getBase64Secret());
        Resource resource = getResource(req);

        if (!checkToken(claims, resource, auth)) {
        	// token校验失败则直接返回不同信息让用户重新登录
            forward(request, response, "403");
            return false;
        }

        String username = (String) claims.get("name");
        //4、生成无状态Token
        StatelessToken token = new StatelessToken(username + ConstantDef.SPLIT_COMA + ((Integer) claims.get("userPlat") == 0 ? "console" : "user"), (String) claims.get("password"), claims.getId(), (Integer) claims.get("userPlat"));
        try {
            //5、委托给Realm进行登录
            getSubject(request, response).login(token);
        } catch (Exception e) {
            logger.error("拒绝访问后发生异常", e);
            forward(request, response, "404");
            return false;
        }
        return true;
    }

    private String getAuth(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    /**
     * 获取资源对象
     *
     * @param req
     * @return
     * @throws IllegalAccessException
     * @throws IOException
     * @throws InvocationTargetException
     */
    private Resource getResource(HttpServletRequest req)
            throws IllegalAccessException, IOException, InvocationTargetException, IllegalDataException {
        Resource resource = new Resource();
        resource.setResourcePath(getResourcePath(req));
        resource = resourceService.getResourceDetail(resource);
        return resource;
    }

    /**
     * 获取请求中的资源路径
     *
     * @param req
     * @return
     */
    private String getResourcePath(HttpServletRequest req) {
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        return uri.replaceFirst(contextPath, "").replaceFirst("/send", "");
    }

	private boolean isOnlineUser(Claims claims){

        if(null == claims || claims.getExpiration().before(new Date())){
            return false;
        }
        String userName = (String) claims.get("name");
        Integer userPlat = (Integer) claims.get("userPlat");
        // 查询在线用户列表中是否存在当前用户
        Map<String, List<String>> userSet = (Map<String, List<String>>) template.opsForValue().get(ConstantDef.ONLINE_USER_SET + userName + ConstantDef.SPLIT_COMA + (userPlat == 0 ? "console" : "user"));
        if (null == userSet) {
            return false;
        }
        String key = userPlat + ConstantDef.SPLIT_COMA + claims.get("roleId") + ConstantDef.SPLIT_COMA + claims.get("loginPlat");
        List<String> list = userSet.get(key);
        if (null == list) {
            return false;
        }
        for (String clientId : list) {
            if (clientId.equals(claims.getId())) {
                return true;
            }
        }

        return false;

    }

   // 校验token
    private boolean checkToken(Claims claims, Resource resource, String auth) throws IllegalDataException {
        // 校验自己是否为当前登录用户(单点登录)
        // 查询在线用户列表中是否存在当前用户
        if(!isOnlineUser(claims)){
            return false;
        }
        // 校验token是否还有效
        if (null == resource.getTokenType() || null == resource.getTokenType().getCheckTimes()) {
            return true;
        }
        // 校验token剩余次数
        Long checkTimesFromRedis = template.opsForValue().increment(ConstantDef.DEFAULT_JWT_CLIENT_ID + "_" + auth, -1);
        if (checkTimesFromRedis < 0) {
            // 次数用完后可以清除信息
            template.delete(ConstantDef.DEFAULT_JWT_CLIENT_ID + "_" + auth);
            return false;
        }
        // 申请的token不能用于访问其他信息
        return claims.get("required") == resource.getTokenType().getRequired() && claims.get("resourcePath").equals(resource.getResourcePath());
    }

    // 使用request转发请求
    private void forward(ServletRequest request, ServletResponse response, String code) throws ServletException, IOException {
        request.getRequestDispatcher("/user/" + code).forward(request, response);
    }

    public RedisTemplate<Serializable, Object> getTemplate() {
        return template;
    }

    public void setTemplate(RedisTemplate<Serializable, Object> template) {
        this.template = template;
    }

    public ResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

}