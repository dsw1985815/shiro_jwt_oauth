package com.dongsw.authority.shiro;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.model.User;
import com.dongsw.authority.model.UserLoginType;
import com.dongsw.authority.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.List;

public class StatelessRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate<Serializable, Object>	template;

	@Override
	public boolean supports(AuthenticationToken token) {
		//仅支持StatelessToken类型的Token  
		return token instanceof StatelessToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 获取用户信息
		String loginInfo = (String) super.getAvailablePrincipal(principals);
		return userService.getSimpleAuthorizationInfoByStatelessRealm(loginInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken statelessToken){
		StatelessToken token = (StatelessToken) statelessToken;
		String[] nameArr = token.getUsername().split(ConstantDef.SPLIT_COMA);
		User user = userService.getUserByName(nameArr[0], nameArr[1]);
		if (null != user)
		{
			List<UserLoginType> typeList = (List<UserLoginType>) template.opsForValue().get(ConstantDef.USER_LOGIN_TYPE_LIST);
			if (null  == typeList || typeList.isEmpty()) {
				typeList = userService.getLoginType();
			}
			template.opsForValue().set(ConstantDef.USER_LOGIN_TYPE_LIST, typeList);
			return new SimpleAuthenticationInfo(token.getUsername(), user.getPassword(), getName());
		}
		return null;
	}

}