package com.dongsw.authority.controller;

import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制层的共性
 * 
 * @auth liaozhanggen 2016-11-19 上午9:42:30
 * 
 */
public class BaseController {

	private Logger logger = LoggerFactory.getLogger(BaseController.class);
	private JsonResult result;

	private static final String PREFIX_MESSAGE = "后端返回数据：";
	private static final String PREFIX_OBJECT_MESSAGE = "后端返回数据对象：{}";
	public String responseSuccess() {
			result = new JsonResult(ResultCode.MSG_SUCCESS, ResultCode.SUCCESS);
		logger.info(PREFIX_MESSAGE + result.toJson());
		return result.toJson();
	}
	
	public String responseSuccess(Object data) {
		logger.info(PREFIX_OBJECT_MESSAGE, data);
		result = new JsonResult(data, ResultCode.MSG_SUCCESS,
				ResultCode.SUCCESS);
		logger.info(PREFIX_MESSAGE+ result.toJson());
		return result.toJson();
	}

	public String responseSuccessToken(String token, Object data) {
		logger.info(PREFIX_OBJECT_MESSAGE, data);
			result = new JsonResult(data, ResultCode.MSG_SUCCESS,
					ResultCode.SUCCESS);
			result.setToken(token);
		logger.info(PREFIX_MESSAGE+ result.toJson());
		return result.toJson();
	}

	public String responseFail() {
		result = new JsonResult(ResultCode.FAILED, ResultCode.MSG_ERROR);
		logger.info(PREFIX_MESSAGE + result.toJson());
		return result.toJson();
	}

	public String responseFail(String massage) {
			result = new JsonResult(massage, ResultCode.FAILED);
		logger.info(PREFIX_MESSAGE + result.toJson());
		return result.toJson();
	}

	public String responseFail(Integer resultCode, String massage) {
			result = new JsonResult(massage, resultCode);
		logger.info(PREFIX_MESSAGE + result.toJson());
		return result.toJson();
	}

}
