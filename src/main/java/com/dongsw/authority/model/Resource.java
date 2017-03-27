package com.dongsw.authority.model;

import org.beetl.sql.core.TailBean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "资源", description = "戚羿辰,董双伟")
public class Resource extends TailBean {

	private static final long	serialVersionUID	= -2845763342257837816L;

	//主键
	@ApiModelProperty(value = "资源id", dataType = "Integer")
	private Integer				id;
	//资源请求路径，如：/order/list/*
	@ApiModelProperty(value = "资源路径", dataType = "String")
	private String				resourcePath;
	@ApiModelProperty(value = "令牌类型ID", dataType = "Integer")
	private Integer				tokenTypeId;
	@ApiModelProperty(value = "是否允许转发header.0否 1是 ,默认0", dataType = "Integer")
	private Integer				allowResetHeader;
	@ApiModelProperty(value = "默认权限admin.如果默认允许访问所有,则设为anon", dataType = "String")
	private String				defaultPermission;
	
	
	/**
	 * 令牌类型id
	 */
	private TokenType			tokenType;

	/**
	 * 资源对应的权限信息
	 */
	private String				permStr;

	/**
	 * 配置信息集合
	 */
	private Map<String, String>	configerMap;

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getPermStr() {
		return permStr;
	}

	public void setPermStr(String permStr) {
		this.permStr = permStr;
	}

	public Integer getTokenTypeId() {
		return tokenTypeId;
	}

	public void setTokenTypeId(Integer tokenTypeId) {
		this.tokenTypeId = tokenTypeId;
	}

	public Integer getAllowResetHeader() {
		return null == allowResetHeader ? 0 : allowResetHeader;
	}

	public void setAllowResetHeader(Integer allowResetHeader) {
		this.allowResetHeader = allowResetHeader;
	}

	public void setConfigerMap(Map<String, String> configerMap) {
		this.configerMap = configerMap;
	}

	public Map<String, String> getConfigerMap() {
		return configerMap;
	}

	public String getDefaultPermission() {
		return null == defaultPermission?"anon":defaultPermission;
	}

	public void setDefaultPermission(String defaultPermission) {
		this.defaultPermission = defaultPermission;
	}
	
}
