package com.dongsw.authority.controller.token;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.common.util.JwtHelper;
import com.dongsw.authority.common.util.ResponseMessageUtil;
import com.dongsw.authority.common.util.ResultStatusCode;
import com.dongsw.authority.common.util.StringUtils;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.controller.BaseController;
import com.dongsw.authority.model.*;
import com.dongsw.authority.service.ResourceService;
import com.dongsw.authority.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */

@RestController
@RequestMapping("oauth")
@Api(value = "获取验证令牌", description = "戚羿辰,董双伟")
public class OauthController extends BaseController {

    private static Logger logger = Logger.getLogger(OauthController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private Audience audience;
    @Autowired
    private RedisTemplate<Serializable, Object> template;

    @ApiOperation(value = "申请令牌", notes = "申请登录token")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @PostMapping(value = "/token/login")
    public String loginToken(@RequestHeader("Authorization") String authorization,
                             @RequestParam("userCode") String userCode) {
        Claims claims = JwtHelper.parseJWT(authorization, audience.getBase64Secret());
        if (null == claims) {
            return responseFail(ResultStatusCode.INVALID_TOKEN.getErrcode(),
                    ResultStatusCode.INVALID_TOKEN.getErrmsg());
        }

        //拼装accessToken
        TokenType tokenType = new TokenType();
        tokenType.setExpire(audience.getDefaultExpire());
        tokenType.setCheckTimes(audience.getDefaultCheckTimes());
        User user = userService.getUser(new Integer(claims.get("userid").toString()),
                claims.get("name").toString().split(ConstantDef.SPLIT_COMA)[1]);
        if (null == user || null == user.getRoles() || user.getRoles().isEmpty()) {
            return responseFail("用户或角色缺失");
        }
        boolean flag = false;
        for (Role role : user.getRoles()) {
            if (role.getUserCode().equals(userCode)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return responseFail("用户或角色缺失");
        }

        claims.put("userCode", userCode);
        claims.put("checkTimes", tokenType.getCheckTimes().toString());

        String accessToken = JwtHelper.createJWT(
                claims.getId(),
                tokenType.getExpire(),
                (Integer)claims.get("roleId"),
                userCode,
                audience.getBase64Secret(),
                claims
        );

        //返回accessToken
        AccessToken accessTokenEntity = new AccessToken();
        accessTokenEntity.setToken(accessToken);
        accessTokenEntity.setExpiresIn(tokenType.getExpire() / 1000);
        accessTokenEntity.setClientId(claims.getId());
        return responseSuccess(accessTokenEntity);
    }

    @ApiOperation(value = "申请令牌", notes = "单独申请一次性令牌方法")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @PostMapping(value = "/token/limit")
    public String limitToken(@RequestHeader("Authorization") String authorization,
                             @RequestParam("redirectUri") String uri) {
        Claims claims = JwtHelper.parseJWT(authorization, audience.getBase64Secret());
        if (null == claims) {
            return responseFail(ResultStatusCode.INVALID_TOKEN.getErrcode(),
                    ResultStatusCode.INVALID_TOKEN.getErrmsg());
        }

        Resource resource = new Resource();
        URI redirectUri = URI.create(uri);
        resource.setResourcePath(redirectUri.getPath());
        try {
            resource = resourceService.getResourceDetail(resource);
        } catch (IllegalDataException e) {
            logger.error("获取特殊token发生异常",e);
            ResponseMessageUtil.whenTrueReturn(true, ResultStatusCode.INVALID_URI.getErrcode(), ResultStatusCode.INVALID_URI.getErrmsg() + "：" + e.getMessage());
        }
        if (resource == null) {
            return responseFail(ResultStatusCode.RESOURCE_NOT_EXIST.getErrcode(),
                    ResultStatusCode.RESOURCE_NOT_EXIST.getErrmsg());
        }
        //拼装accessToken
        TokenType tokenType = resource.getTokenType();
        if (tokenType == null) {
            tokenType = new TokenType();
            tokenType.setExpire(audience.getDefaultExpire());
            tokenType.setCheckTimes(audience.getDefaultCheckTimes());
        }

        claims.put("checkTimes", tokenType.getCheckTimes().toString());
        String accessToken = JwtHelper.createJWT(
                claims.getId(),
                tokenType.getExpire(),
                (Integer)claims.get("roleId"),
                (String)claims.get("userCode"),
                audience.getBase64Secret(),
                claims
        );

        // 对使用次数有限制的token进行设置
        if (null != tokenType.getCheckTimes()) {
            template.opsForValue().set(ConstantDef.DEFAULT_JWT_CLIENT_ID + "_" + authorization,
                    tokenType.getCheckTimes());
        }
        //返回accessToken
        AccessToken accessTokenEntity = new AccessToken();
        accessTokenEntity.setToken(accessToken);
        accessTokenEntity.setExpiresIn(tokenType.getExpire() / 1000);
        accessTokenEntity.setClientId(claims.getId());
        return responseSuccess(accessTokenEntity);
    }

    @ApiOperation(value = "申请令牌", notes = "申请令牌方法")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS_CREATED, message = "添加成功返回结果")})
    @PostMapping(value = "/token")
    public String login(@RequestBody LoginPara loginPara) {
        if (StringUtils.isEmpty(loginPara.getClientId()) || StringUtils.isEmpty(loginPara.getUserName()) || StringUtils.isEmpty(loginPara.getPassword())) {
            Long clientId = template.opsForValue().increment(ConstantDef.DEFAULT_JWT_CLIENT_ID, 1);
            loginPara.setClientId(clientId.toString());
        }

        ResponseMessageUtil.whenTrueReturn(loginPara.getRedirectUri() == null, ResultStatusCode.INVALID_URI.getErrcode(), ResultStatusCode.INVALID_URI.getErrmsg());
        // 将不足的参数填充
        Resource resource = new Resource();
        URI redirectUri = URI.create(loginPara.getRedirectUri());
        resource.setResourcePath(redirectUri.getPath());
        try {
            resource = resourceService.getResourceDetail(resource);
            ResponseMessageUtil.whenTrueReturn(resource == null, ResultStatusCode.RESOURCE_NOT_EXIST.getErrcode(), ResultStatusCode.RESOURCE_NOT_EXIST.getErrmsg());
            setLoginParam(loginPara,resource);
        }  catch (IllegalAccessException | InvocationTargetException | IllegalDataException e) {
            logger.error("获取特殊token发生异常",e);
            ResponseMessageUtil.whenTrueReturn(true, ResultStatusCode.INVALID_URI.getErrcode(), ResultStatusCode.INVALID_URI.getErrmsg() + "：" + e.getMessage());
        }
        //拼装accessToken
        AccessToken accessTokenEntity = getToken(loginPara, resource);

        return responseSuccess(accessTokenEntity);
    }

    // 将不足的参数填充
    private void setLoginParam(LoginPara loginPara,Resource resource) throws IllegalAccessException, InvocationTargetException {
        //验证用户名密码-- 该接口不开放,由内部转发.只有通过校验的才会到这里
        User user = userService.getUserByName(loginPara.getUserName(), loginPara.getPlatformId());
        loginPara.setCheckTimes(resource.getTokenType().getCheckTimes());
        loginPara.setRedirectUri("/Authority/user/login");
        loginPara.setTtlmillis(resource.getTokenType().getExpire());
        loginPara.setUserId(user.getId());
    }

    //拼装accessToken
    private AccessToken getToken(LoginPara loginPara, Resource resource) {
        TokenType tokenType = resource.getTokenType();
        if (tokenType == null) {
            tokenType = new TokenType();
            tokenType.setExpire(audience.getDefaultExpire());
            tokenType.setCheckTimes(audience.getDefaultCheckTimes());
        }
        String accessToken = JwtHelper.createJWT(loginPara);
        if (null != tokenType.getCheckTimes()) {
            template.opsForValue().set(ConstantDef.DEFAULT_JWT_CLIENT_ID + "_" + accessToken, tokenType.getCheckTimes());
        }
        //返回accessToken
        AccessToken accessTokenEntity = new AccessToken();
        accessTokenEntity.setToken(accessToken);
        accessTokenEntity.setExpiresIn(tokenType.getExpire() / 1000);
        accessTokenEntity.setClientId(loginPara.getClientId());
        return accessTokenEntity;
    }
}