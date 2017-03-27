package com.dongsw.authority.common.def;

import java.io.Serializable;

public class ConstantDef {
	/**
	 * 有效
	 */
	public static final Integer		VALID						= 1;
	/**
	 * 失效
	 */
	public static final Integer		INVALID						= 0;
	/**
	 * 资源最近一次更新时间
	 */
	public static final String			RESOURCE_DEFAULT_UPDATE		= "RESOURCE_DEFAULT_UPDATE";
	/**
	 * 资源列表
	 */
	public static final String			RESOURCE_DEFAULT_URL_LIST	= "RESOURCE_DEFAULT_URL_LIST";

	/**
	 * 用户名分隔符
	 */
	public static final String			SPLIT_COMA					= "#";

	/**
	 * 用户名分隔符
	 */
	public static final String			PATH_SLASH					= "/";
	/**
	 * 默认分页大小
	 */
	public static final String			DEFAULT_PAGESIZE			= "10";
	/**
	 * 当前页面
	 */
	public static final String			DEFAULT_PAGENO				= "1";
	/**
	 * 资源过滤器存放地址
	 */
	public static final Serializable	RESOURCE_FILTER_SET			= "RESOURCE_FILTER_SET";
	/**
	 * jwt-clientId
	 */
	public static final Serializable	DEFAULT_JWT_CLIENT_ID		= "DEFAULT_JWT_CLIENT_ID";
	/**
	 * jwt-token
	 */
	public static final Object			DEFAULT_JWT_CLIENT_TOKEN	= "DEFAULT_JWT_CLIENT_TOKEN_";
	/**
	 * url对应map的前缀
	 */
	public static final String			URI_PRE						= "service.host.";
	/**
	 * 在线用户个人信息列表
	 */
	public static final String			ONLINE_USER_SET				= "ONLINE_USER_SET_";
	/**
	 * 单点登录类型
	 */
	public static final Integer		LOGIN_TYPE_SINGLE			= 0;
	/**
	 * 多点登录类型
	 */
	public static final Integer		LOGIN_TYPE_MULI				= 1;
	/**
	 * web登录类型
	 */
	public static final Integer		LOGIN_PLAT_WEB				= 0;
	/**
	 * ios登录类型
	 */
	public static final Integer		LOGIN_PLAT_IOS				= 1;
	/**
	 * Android登录类型
	 */
	public static final Integer		LOGIN_PLAT_ANDROID			= 2;
	/**
	 * 用户登录类型列表
	 */
	public static final String			USER_LOGIN_TYPE_LIST		= "USER_LOGIN_TYPE_LIST_";
	/**
	 * 在线用户集合
	 */
	public static final String			ONLINE_USERS				= "ONLINE_USERS_";
	/**
	 * 图片验证码需要用到的自定义信息
	 */
	public static final String			DEFAULT_VERIFY_TOKEN		= "DEFAULT_VERIFY_TOKEN";


	private ConstantDef() {
	}
}
