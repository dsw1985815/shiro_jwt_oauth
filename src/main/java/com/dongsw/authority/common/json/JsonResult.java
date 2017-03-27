package com.dongsw.authority.common.json;


import com.dongsw.authority.common.def.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 返回对象Result封装
 * @athor 顾文伟
 * @date 2016-11-04
 * @version 1.0
 * 
 * @update code liaozhanggen 2016-11-23 14
 */
@ApiModel(value = "Json对象", description = "传输JSON对象数据模型")
public class JsonResult implements Serializable{

	/**
	 * @auth liaozhanggen 2016-11-23 下午1:38:50
	 */
	private static final long serialVersionUID = -5192270064169684048L;
	
	/**
	 * 状态码(200代表成功，其他可自行定义)，失败可用 404，500....之类参考 status.java类中的定义 
	 * 
	 * @author update code liaozhanggen 2016-11-23 14
	 */
	@ApiModelProperty(value = "结果状态码", required = true)
	private int status;
	/**
	 * 提示消息
	 */
	@ApiModelProperty(value = "结果提示信息", required = true)
	private String message = "";

	/**
	 * 数据对象
	 */
	@ApiModelProperty(value = "传输数据对象", required = true)
	private Object data;
	
	/**
	 * token信息
	 */
	@ApiModelProperty(value = "token", required = true)
	private String token;


	public JsonResult() {
		status = ResultCode.SUCCESS;
	}

	public JsonResult(Object data, String message) {
		this.data = data;
		this.status = ResultCode.SUCCESS;
		this.message = message;
	}

	public JsonResult(List<?> data, String message, int status) {
		this.data = data;
		this.status = status;
		this.message = message;
	}

	public JsonResult(Object data, String message, int status) {
		this.data = data;
		this.status = status;
		this.message = message;
	}


	public JsonResult(String message, int status) {
		this.status = status;
		this.message = message;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		
		this.data = data;
	}



	public String toJson() {
		return JsonUtil.toJson(this);
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
