package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
* 
* gen by beetlsql 2017-02-17
*/
@ApiModel(value = "token缓存", description = "戚羿辰,董双伟")
public class AccessTokenCache {

	@ApiModelProperty(value = "id", dataType = "Integer")
	private Integer	id;
	//token的真实值
	@ApiModelProperty(value = "token", dataType = "String")
	private String	accessToken;
	//唯一标识
	@ApiModelProperty(value = "jwt id", dataType = "String")
	private String	jti;
	//刷新时间戳
	@ApiModelProperty(value = "过期时间", dataType = "String")
	private String	rexp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public String getRexp() {
		return rexp;
	}

	public void setRexp(String rexp) {
		this.rexp = rexp;
	}

}
