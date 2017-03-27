package com.dongsw.authority.controller;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.exception.IllegalDataException;
import com.dongsw.authority.common.exception.RespExcption;
import com.dongsw.authority.common.util.JwtHelper;
import com.dongsw.authority.common.util.ResponseMessageUtil;
import com.dongsw.authority.common.util.ResultStatusCode;
import com.dongsw.authority.conf.Audience;
import com.dongsw.authority.controller.token.OauthController;
import com.dongsw.authority.model.Role;
import com.dongsw.authority.model.TokenType;
import com.dongsw.authority.model.User;
import com.dongsw.authority.model.UserLoginType;
import com.dongsw.authority.model.verify.GeetestConfig;
import com.dongsw.authority.model.verify.GeetestLib;
import com.dongsw.authority.service.ResourceService;
import com.dongsw.authority.service.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@RestController
@RequestMapping("/user")
@SuppressWarnings("unchecked")
@Api(value = "用户相关", description = "戚羿辰,董双伟")
public class UserController extends BaseController {

    private static Logger logger = Logger.getLogger(OauthController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<Serializable, Object> template;
    @Autowired
    private Audience audience;

    @Autowired
    private ResourceService resourceService;

    @ApiOperation(value = "更新用户单点登录状态  last 2017/3/15", notes = "更新用户单点登录状态")
    @PutMapping("/loginType")
    public String updateLoginType(@RequestBody UserLoginType userLoginType) {
        return responseSuccess(userService.updateLoginType(userLoginType));
    }

    @ApiOperation(value = "用户登录  last 2017/2/23", notes = "用户登录")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS, message = ResultCode.MSG_SUCCESS)})
    @PostMapping(value = "/login")
    public String login(
            @RequestHeader("Authorization") String token,
            @RequestHeader(required = false, value = GeetestLib.FN_GEETEST_CHALLENGE) String challenge,
            @RequestHeader(required = false, value = GeetestLib.FN_GEETEST_VALIDATE) String validate,
            @RequestHeader(required = false, value = GeetestLib.FN_GEETEST_SECCODE) String seccode,
            @RequestParam(value = "userCode", required = true) String userCode
    ) {
        Claims claim = JwtHelper.parseJWT(token, audience.getBase64Secret());
        ResponseMessageUtil.whenNullReturn(claim, "令牌错误");

        /**
         如果需要校验,则需要带入验证信息
         if (someConditions) {
         GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key());
         //从session中获取userid
         int gtResult = 0;
         gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, ConstantDef.DEFAULT_VERIFY_TOKEN);
         ResponseMessageUtil.assertEqu(gtResult == 1, "图片验证失败,请重试");
         }
         **/
        Long clientId = template.opsForValue().increment(ConstantDef.DEFAULT_JWT_CLIENT_ID, 1);

        // 登录地址
        String uri = String.valueOf(claim.get("resourcePath"));
        // 根据地址获取token类型
        TokenType tokenType = null;
        try {
            tokenType = resourceService.getTokenType(uri);
        } catch (IllegalDataException e) {
            logger.error("获取token异常", e);
            ResponseMessageUtil.whenTrueReturn(true, ResultStatusCode.INVALID_TOKEN.getErrcode(), ResultStatusCode.INVALID_TOKEN.getErrmsg() + "：" + e.getMessage());
        }
        // 用户id
        Integer userId = (Integer) claim.get("userid");
        // 用户平台类型,0控制台 1劳务 2商城
        Integer userPlat = (Integer) claim.get("userPlat");
        // 用户登录平台,0.web 1.ios 2.app
        Integer loginPlat = (Integer) claim.get("loginPlat");

        // 校验当前userCode是否属于该userId,避免传别人的userCode
        User user = userService.getUser(userId, userPlat == 0 ? "console" : "user");
        Role role = checkSelf(userCode, user);

        //没有角色则不能登录
        if (null == role) {
            throw new RespExcption(ResultCode.FAILED, "角色为空，登录失败");
        }

        Integer expire = -1;
        if (tokenType != null && tokenType.getCheckTimes() != null) {
            claim.put("checkTmes", tokenType.getCheckTimes().toString());
        }

        if (tokenType != null && tokenType.getExpire() != null) {
            expire = tokenType.getExpire();
        }

        String accessToken = getToken(
                clientId.toString(),
                expire,
                role.getId(),
                userCode,
                audience.getBase64Secret(),
                claim
        );

        // 记录redis登录状态,任何登录操作都会永久保存到redis.用于获取对应平台的单点或多点登录状态
        setLoginStatus(loginPlat, userPlat, role, claim, clientId);
        return responseSuccessToken(accessToken, clientId);
    }

    private void setLoginStatus(Integer loginPlat, Integer userPlat, Role role, Claims claim, Long clientId) {
        List<UserLoginType> typeList = (List<UserLoginType>) template.opsForValue().get(ConstantDef.USER_LOGIN_TYPE_LIST);
        if (null  == typeList || typeList.isEmpty()) {
            typeList = userService.getLoginType();
        }
        template.opsForValue().set(ConstantDef.USER_LOGIN_TYPE_LIST, typeList);
        Integer type = ConstantDef.LOGIN_TYPE_SINGLE;
        for (UserLoginType loginType : typeList) {
            if (!(loginType.getLoginPlat().equals(loginPlat) && loginType.getPlatType() == userPlat && loginType.getUserRole() == role.getId())) {
                continue;
            }
            type = loginType.getLoginType();
        }
        Map<String, List<String>> userSet = (Map<String, List<String>>) template.opsForValue().get(ConstantDef.ONLINE_USER_SET + claim.get("name") + ConstantDef.SPLIT_COMA + (userPlat == 0 ? "console" : "user"));
        if (null == userSet) {
            userSet = new HashMap<>();
        }
        String key = userPlat + ConstantDef.SPLIT_COMA + role.getId() + ConstantDef.SPLIT_COMA + loginPlat;
        if (type == ConstantDef.LOGIN_TYPE_SINGLE) {
            userSet.remove(key);
            List<String> list = new ArrayList<>();
            list.add(clientId.toString());
            userSet.put(key, list);
        } else {
            List<String> list = userSet.get(key);
            if (null == list) {
                list = new ArrayList<>();
            }
            list.add(clientId.toString());
            userSet.put(key, list);
        }
        template.opsForValue().set(ConstantDef.ONLINE_USER_SET + claim.get("name") + ConstantDef.SPLIT_COMA + (userPlat == 0 ? "console" : "user"), userSet);
    }

    // 校验userCode和userId匹配关系
    private Role checkSelf(String userCode, User user) {
        Role roleObj = null;
        boolean isSelf = false;
        if (null == user || null == user.getRoles() || user.getRoles().isEmpty()) {
            throw new RespExcption(ResultCode.FAILED, "用户或角色不存在");
        }
        for (Role role : user.getRoles()) {
            if (userCode.equals(role.getUserCode())) {
                isSelf = true;
                roleObj = role;
                break;
            }
        }
        ResponseMessageUtil.whenTrueReturn(!isSelf, "没有权限");
        return roleObj;
    }

    // 根据参数获取token
    private String getToken(String id, long expire, Integer roleId, String userCode, String base64Secret, Claims claim) {
        return JwtHelper.createJWT(
                id,
                expire,
                roleId,
                userCode,
                base64Secret,
                claim
        );


    }

    @ApiOperation(value = "初始化验证码  last 2017/3/18", notes = "初始化验证码")
    @ApiResponses({@ApiResponse(code = ResultCode.SUCCESS, message = ResultCode.MSG_SUCCESS)})
    @GetMapping(value = "/initVerify")
    public String init(HttpServletRequest request, HttpServletResponse response) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.GEETESTID, GeetestConfig.GEETESTKEY);
        gtSdk.preProcess(ConstantDef.DEFAULT_VERIFY_TOKEN);
        String resStr = gtSdk.getResponseStr();
        return responseSuccess(resStr);
    }

    @GetMapping
    @ApiOperation(value = "查找用户  last 2017/2/23", notes = "筛选查找用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", required = false, value = "用户名称"),
            @ApiImplicitParam(paramType = "query", name = "platType", dataType = "String", required = false, value = "平台类型"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = false, value = "分页大小,默认10", defaultValue = ConstantDef.DEFAULT_PAGESIZE),
            @ApiImplicitParam(paramType = "query", name = "pageNo", dataType = "Integer", required = false, value = "页码,默认1", defaultValue = ConstantDef.DEFAULT_PAGENO),})
    public String queryUser(String username, String platType, Integer pageSize, Integer pageNo) {
        return responseSuccess(userService.query(username, platType, pageSize, pageNo));
    }

    @ApiOperation(value = "查找用户  last 2017/2/23", notes = "根据平台和id查找用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Integer", required = false, value = "id"),
            @ApiImplicitParam(paramType = "path", name = "platType", dataType = "String", required = false, value = "平台类型"),})
    @GetMapping("/{id}/{platType}")
    public String getUser(@PathVariable(value = "id") Integer userId,
                          @PathVariable(value = "platType") String platType) {
        return responseSuccess(userService.getUser(userId, platType));
    }


    @ApiOperation(value = "退出登录  last 2017/2/23", notes = "退出登录")
    @GetMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        Claims claim = JwtHelper.parseJWT(token, audience.getBase64Secret());
        ResponseMessageUtil.whenNullReturn(claim, "令牌错误");
        // 用户平台类型,0控制台 1劳务 2商城
        Integer userPlat = (Integer) claim.get("userPlat");
        // clientId
        String clientId = claim.getId();
        String key = ConstantDef.ONLINE_USER_SET + claim.get("name") + ConstantDef.SPLIT_COMA + (userPlat == 0 ? "console" : "user");
        Map<String, List<String>> userMap = (Map<String, List<String>>) template.opsForValue().get(key);
        if (null == userMap || userMap.isEmpty()) {
            return responseSuccess("已经安全退出登录");
        }
        setKey(userMap, clientId, key);
        return responseSuccess("已经安全退出登录");
    }

    @ApiOperation(value = "查询用户登录状态  last 2017/3/15")
    @GetMapping("/loginType/{username}/{platType}")
    public String getLoginType(@PathVariable(value = "username") String username,
                               @PathVariable(value = "platType") String platType) {
        String key = ConstantDef.ONLINE_USER_SET + username + ConstantDef.SPLIT_COMA + platType;
        Map<String, List<String>> userMap = (Map<String, List<String>>) template.opsForValue().get(key);
        if (null == userMap || userMap.isEmpty()) {
            return responseSuccess();
        }
        List<Map<String, Object>> userList = new ArrayList<>();
        for (Entry<String, List<String>> map : userMap.entrySet()) {
            for (String clientId : map.getValue()) {
                Map<String, Object> userOnlineMap = new HashMap<>();
                String[] keyArr = map.getKey().split(ConstantDef.SPLIT_COMA);
                userOnlineMap.put("userPlat", keyArr[0]);
                userOnlineMap.put("roleId", keyArr[1]);
                userOnlineMap.put("loginPlat", keyArr[2]);
                userOnlineMap.put("clientId", clientId);
                userList.add(userOnlineMap);
            }
        }
        return responseSuccess(userList);
    }


    private void setKey(Map<String, List<String>> userMap, String clientId, String key) {
        String mapKey = null;
        for (Entry<String, List<String>> map : userMap.entrySet()) {
            for (String cid : map.getValue()) {
                // 移除客户端在线id
                if (cid.equals(clientId)) {
                    map.getValue().remove(cid);
                    mapKey = map.getKey();
                    break;
                }
            }

        }
        if (null != mapKey) {
            if (null == userMap.get(key) || userMap.get(key).isEmpty()) {
                userMap.remove(mapKey);
            }
            // 如果不存在其他登录平台,则移除该用户登录状态
            if (userMap.isEmpty()) {
                template.delete(key);
            }
        }
        if (!userMap.isEmpty()) {
            template.opsForValue().set(key, userMap);
        }
    }
}
