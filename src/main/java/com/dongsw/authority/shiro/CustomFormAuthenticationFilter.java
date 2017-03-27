package com.dongsw.authority.shiro;

import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.common.util.JwtHelper;
import com.dongsw.authority.common.util.StringUtils;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.model.Resource;
import com.dongsw.authority.service.ResourceService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 自定义过滤类
 * Created by 戚羿辰 on 2017/02/20。
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomFormAuthenticationFilter.class);


    RedisTemplate<Serializable, Object> template;

    ResourceService resourceService;

    private Audience audience;

    public void setTemplate(RedisTemplate<Serializable, Object> template) {
        this.template = template;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
    
    /**
     * 获取资源是否强制验证token
     *
     * @param resource
     * @return
     */
    private boolean isRequiredTokenResource(Resource resource) {
        return resource.getTokenType() != null && resource.getTokenType().getRequired() == 1;
    }

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		boolean isLogined = super.isAccessAllowed(request, response, mappedValue)
				|| (!isLoginRequest(request, response) && isPermissive(mappedValue));
		HttpServletRequest req = (HttpServletRequest) request;
		String auth = getAuth(req);
		try
		{
			Resource resource = getResource(req);
			if (resource == null)
			{
				return false;
			}
			boolean isRequiredTokenResource = isRequiredTokenResource(resource);

			//token不为空则验证token
			if (auth != null)
			{
				return checkToken(auth, resource, req);
			}
			//以下是token为空的逻辑

			//登录失败
			if (!isLogined )
			{
				return false;
			}
			//登陆成功但token为空，返回当前资源是否是必须token的资源
            return !isRequiredTokenResource;

		} catch (IllegalAccessException e)
		{
			logger.error("非法的访问", e);
			return false;
		} catch (IOException e)
		{
			logger.error("IO交互异常", e);
			return false;
		} catch (InvocationTargetException e)
		{
			logger.error("调用目标异常", e);
			return false;
		} catch (IllegalDataException e)
		{
			logger.error("非法数据", e);
			return false;
		}
	}

    /**
     * 检查token和资源是否合法
     *
     * @param auth
     * @param resource
     * @param req
     * @return
     */
    private boolean checkToken(String auth, Resource resource, HttpServletRequest req) throws IllegalDataException {
        Claims claims = JwtHelper.parseJWT(auth, audience.getBase64Secret());
        //token解析失败返回
        if (claims == null) {
            return false;
        }
        Integer userid = (Integer) claims.get("userid");
        String authKey = auth.substring(auth.length() - 16, auth.length());
        //缓存当中的token使用次数
        Long checkTimesFromRedis = template.opsForValue().increment(authKey, -1);
        //次数已经用完返回
        if (checkTimesFromRedis < 0) {
            return false;
        }
        //检查用户是否token生成时的同一个用户，需要通过配置来获取资源路径上的请求用户id
        //如果资源配置为空 则不做用户id校验
        if (resource.getConfigerMap() != null && resource.getConfigerMap().size() != 0) {
            String requestUserId = getUseridFromRequest(resource.getConfigerMap(), req);
            //token中userid与当前资源路径上的userid不同则返回
            if (!StringUtils.isNumber(requestUserId) || !userid.equals(Integer.parseInt(requestUserId))) {
                return false;
            }
        }
        //返回token中required和resourcePath是否一致
        if (resource.getTokenType().getRequired() == 0) {
        	return true;
        }
        return claims.get("required") == resource.getTokenType().getRequired()&&claims.get("resourcePath").equals(resource.getResourcePath());

    }

    private String getAuth(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String auth = getAuth(req);
        if (auth != null) {
            Claims claims = JwtHelper.parseJWT(auth, audience.getBase64Secret());
            if(claims!=null){
                String password = (String) claims.get("password");
                String name = (String) claims.get("name");
                AuthenticationToken token = new UsernamePasswordToken(name, password);
                getSubject(request, response).login(token);
            }
        }
        //具体响应ShiroDbRealm。doGetAuthorizationInfo，判断是否包含此url的响应权限, 修改方法,避免response跳转而直接返回错误
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
		} else
		{
			// 未登录则不用框架的response转发,直接使用request转发
			request.getRequestDispatcher("/user/404").forward(request, response);
			return false;
		}
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
    private Resource getResource(HttpServletRequest req) throws IllegalAccessException, IOException, InvocationTargetException, IllegalDataException {
        Resource resource = new Resource();
        resource.setResourcePath(getResourcePath(req));
        resource = resourceService.getResourceDetail(resource);
        return resource;
    }

    /**
     * 获取请求对应的用户id
     *
     * @param configerMap
     * @param request
     * @return
     */
    private String getUseridFromRequest(Map<String, String> configerMap, HttpServletRequest request) throws IllegalDataException {
        String userid = null;
        String type = configerMap.get("type");
        if (StringUtils.isEmpty(type)) {
            throw new IllegalDataException("资源配置详情错误,type未定义");
        }
        String paramUserId = configerMap.get("paramUserId");
        String[] pathparam = getResourcePath(request).split("/");
        switch (type) {
            case "1"://path
                String position = configerMap.get("position");
                if (!StringUtils.isEmpty(position) && StringUtils.isNumber(position)) {
                    userid = pathparam[Integer.parseInt(position)];
                }
                break;
            case "2"://param
                userid = request.getParameter(paramUserId);
                break;
            case "3"://header
                userid = request.getHeader(paramUserId);
                break;
            default://attribute
                userid = (String) request.getAttribute(paramUserId);
        }
        if (userid == null) {
            throw new IllegalDataException("资源配置详情错误,未找到对应的请求参数");
        }
        return userid;
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
}