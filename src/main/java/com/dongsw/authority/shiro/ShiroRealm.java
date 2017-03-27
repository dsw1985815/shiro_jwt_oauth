package com.dongsw.authority.shiro;

import com.dongsw.authority.common.def.ConstantDef;
import com.dongsw.authority.model.User;
import com.dongsw.authority.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 获取用户信息
		String loginInfo = (String) super.getAvailablePrincipal(principals);
		return userService.getSimpleAuthorizationInfoByShiroRealm(loginInfo);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String[] nameArr = token.getUsername().split(ConstantDef.SPLIT_COMA);
		User user = userService.getUserByName(nameArr[0], nameArr[1]);
		if (null != user)
		{
			return new SimpleAuthenticationInfo(token.getUsername(), user.getPassword(), getName());
		}
		return null;
	}

	public void flushAuthorizationInfo(PrincipalCollection principals) {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		Object key = getAuthorizationCacheKey(principals);
		cache.put(key, doGetAuthorizationInfo(principals));
	}
}
