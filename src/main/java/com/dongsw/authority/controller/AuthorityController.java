
package com.dongsw.authority.controller;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.common.exception.RespExcption;
import com.dongsw.authority.common.json.JsonResult;
import com.dongsw.authority.common.json.JsonUtil;
import com.dongsw.authority.common.model.CustomRestTemplate;
import com.dongsw.authority.common.util.HttpUtil;
import com.dongsw.authority.common.util.JwtHelper;
import com.dongsw.authority.common.util.MD5;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.controller.token.OauthController;
import com.dongsw.authority.model.LoginPara;
import com.dongsw.authority.service.ResourceService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 权限控制controller Created by 戚羿辰 on 2017/02/17。
 */
@RestController
@RequestMapping("/send/**")
@Api(value = "过滤所有", description = "戚羿辰, 董双伟")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AuthorityController extends BaseController {

	private static Logger logger = Logger.getLogger(OauthController.class);

	@Autowired
	private CustomRestTemplate template;
	@Resource(name = "pathMap")
	private Map<String, String> pathMap = null;
	@Autowired
	private Audience audience = null;
	@Autowired
	private ResourceService resourceService = null;
	@Autowired
	private RedisTemplate<Serializable, Object> redisTemplate;
	@Value("${service.host.token}")
	private String jwtTokenService;

	@ApiOperation(value = "转发入口", notes = "所有类型的请求均能处理")
	@RequestMapping()
	public String all(@RequestBody(required = false) Object requestBody,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity<JsonResult> result;
		result = sendReq(request, requestBody);
		if (null == result) {
			return responseFail();
		}
		return JsonUtil.toJson(result.getBody());
	}

	/**
	 * 发送请求
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private ResponseEntity<JsonResult> sendReq(HttpServletRequest request,
			Object requestBody) {
		HttpHeaders headers = new HttpHeaders();
		Map<String, String[]> params = request.getParameterMap();

		// 设置header
		try {
			setHeader(request, headers);
		} catch (IllegalAccessException | IOException
				| InvocationTargetException | IllegalDataException e) {
			logger.error("发送请求异常", e);
			throw new RespExcption(ResultCode.FAILED, e.getMessage());
		}

		// query类型的属性以url拼接方式传,requestbody直接原样进行转发
		HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody,
				headers);
		String url = getRedirectUrl(request);
		url = HttpUtil.urlFormat(url, params);
		ResponseEntity<JsonResult> resultMap = template.exchange(url,
				HttpUtil.getMethod(request.getMethod()), requestEntity,
				JsonResult.class);
		// 如果是登录请求并且登录成功,则将用户在本系统重新登录
		boolean isLoginUrl = isLoginUrl(url);
		boolean isLogoutUrl = isLogoutUrl(url);
		if (isLoginUrl && null != resultMap
				&& ResultCode.SUCCESS == resultMap.getBody().getStatus()) {
			// 发送参数获取一次性token
			Long clientId = redisTemplate.opsForValue()
					.increment(ConstantDef.DEFAULT_JWT_CLIENT_ID, 1);
			LoginPara loginParam = getLoginParam(requestBody, url, clientId);
			ResponseEntity<JsonResult> resp = template.exchange(
					jwtTokenService + "/Authority/oauth/token", HttpMethod.POST,
					new HttpEntity<>(loginParam), JsonResult.class);
			if (ResultCode.SUCCESS != resp.getBody().getStatus()) {
				return resp;
			}
			Map<String, Object> accessToken = (Map<String, Object>) resp
					.getBody().getData();
			resultMap.getBody().setToken(accessToken.get("token").toString());
			return resultMap;
		}
		if (isLogoutUrl && null != resultMap
				&& ResultCode.SUCCESS == resultMap.getBody().getStatus()) {
			// 将当前用户退出登录
			headers.set("Authorization", request.getHeader("Authorization"));
			return template.exchange(jwtTokenService + "/Authority/user/logout",
					HttpMethod.GET, new HttpEntity<>(null, headers),
					JsonResult.class);
		}
		return resultMap;
	}

	private boolean isLogoutUrl(String url) {
		return url.matches("[^,]*/web/logout");
	}

	private boolean isLoginUrl(String url) {
		boolean flag;
		if (url.matches("[^,]*/shixiaobao/app/TJLW00020/[^,]*/[^,]*/[^,]*")) {
			flag = true;
		} else if (url.matches("[^,]*/console/KZT0702001/accountmgt/login")) {
			flag = true;
		} else if (url.matches("[^,]*/web/login/[^,]*/[^,]*/[^,]*")) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	private LoginPara getLoginParam(Object requestBody, String url,
			Long clientId) {
		Integer loginPlat = null;
		String platformId = null;
		String password = null;
		String username = null;
		Integer userPlat = null;
		Integer needVerify = null;
		if (url.contains("/console")) {
			Map param = JsonUtil.toObject(requestBody.toString(), Map.class);
			username = (String) param.get("userAccount");
			password = MD5.getPassword((String) param.get("operatorPwd"));
			platformId = "console";
			loginPlat = 0;
			userPlat = 0;
			needVerify = 0;
		} else if (url.contains("/shixiaobao/app")) {
			username = url.split("/")[6];
			password = MD5.getPassword(url.split("/")[7]);
			platformId = "user";
			loginPlat = Integer.valueOf(url.split("/")[8]);
			userPlat = 1;
			needVerify = 0;
		} else if (url.contains("/web/login")) {
			username = url.split("/")[5];
			password = MD5.getPassword(url.split("/")[6]);
			platformId = "user";
			loginPlat = Integer.valueOf(url.split("/")[7]);
			userPlat = 2;
			needVerify = 1;
		}

		LoginPara loginParam = new LoginPara();
		loginParam.setClientId(clientId.toString());
		loginParam.setBase64Security(audience.getBase64Secret());
		loginParam.setLoginPlat(loginPlat);
		loginParam.setPassword(password);
		loginParam.setUserPlat(userPlat);
		loginParam.setPlatformId(platformId);
		loginParam.setRedirectUri("/Authority/user/login");
		loginParam.setUserName(username);
		loginParam.setNeedVerify(needVerify);
		return loginParam;
	}

	private void setHeader(HttpServletRequest request, HttpHeaders headers)
			throws IllegalAccessException, IOException,
			InvocationTargetException, IllegalDataException {
		com.dongsw.authority.model.Resource resource = getResource(request);
		String token = request.getHeader("Authorization");
		Claims claims = JwtHelper.parseJWT(token, audience.getBase64Secret());
		if (null != claims && null != resource
				&& resource.getAllowResetHeader() == ConstantDef.INVALID) {
			headers.add("userCode", String.valueOf(claims.get("userCode")));
		} else {
			headers.add("userCode", request.getHeader("userCode"));
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
	private com.dongsw.authority.model.Resource getResource(
			HttpServletRequest req) throws IllegalAccessException, IOException,
			InvocationTargetException, IllegalDataException {
		com.dongsw.authority.model.Resource resource = new com.dongsw.authority.model.Resource();
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

	// 根据传入的urrl获取转发地址, 注册中心建立后从注册中心获取
	private String getRedirectUrl(HttpServletRequest request) {
		String path = request.getRequestURI()
				.substring(request.getRequestURI().indexOf('/', 2));
		String pre;
		// 施小包app项目由于url不规范,在无注册中心对应时特殊处理
		if (request.getRequestURI().contains("/shixiaobao/app")) {
			pre = pathMap.get(ConstantDef.URI_PRE + "shixiaobao/app");
		} else {
			pre = pathMap.get(ConstantDef.URI_PRE + path.split("/")[2]);
		}
		return null == pre
				? request.getRequestURL().toString()
				: pre + path.substring(path.indexOf('/', 2));
	}

	;

}
