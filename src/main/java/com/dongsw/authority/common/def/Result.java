package com.dongsw.authority.common.def;

public class Result {
	private Integer code;
	private String msg;
	private Class<?> excptionClass;

	public Result(Integer code, String msg, Class<?> excptionClass) {
		this.code = code;
		this.msg = msg;
		this.excptionClass = excptionClass;
	}
	public Result(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Class<?> getExcptionClass() {
		return excptionClass;
	}
	public void setExcptionClass(Class<?> excptionClass) {
		this.excptionClass = excptionClass;
	}
	@Override
	public String toString() {
		return "Result [msg=" + msg + ", excptionClass=" + excptionClass + "]";
	}

}
