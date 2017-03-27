package com.dongsw.authority.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/21
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
@ApiModel(value = "令牌类型", description = "戚羿辰,董双伟")
public class TokenType {

	@ApiModelProperty(value = "令牌类型id", dataType = "Integer")
	private Integer	id;
	@ApiModelProperty(value = "token验证次数 0表示不限次数", dataType = "Integer")
	private Integer	checkTimes;
	@ApiModelProperty(value = "超时时间默认2天  单位是毫秒", dataType = "Integer")
	private Integer	expire;
	@ApiModelProperty(value = "描述", dataType = "String")
	private String	describe;
	@ApiModelProperty(value = "令牌名称", dataType = "String")
	private String	name;
	@ApiModelProperty(value = "是否必须强制验证", dataType = "Integer")
	private Integer	required;

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCheckTimes() {
		return checkTimes;
	}

	public void setCheckTimes(Integer checkTimes) {
		this.checkTimes = checkTimes;
	}

	public Integer getExpire() {
		return expire;
	}

	public void setExpire(Integer expire) {
		this.expire = expire;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}