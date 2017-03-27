package com.dongsw.authority.common.util;

import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

	private HttpUtil() {
	}

	/**
	 * 拼接url参数
	 * @param url
	 * @param params
	 * @return
	 */
	public static String urlFormat(String url, Map<String, String[]> params) {
		String urlResult =url;
		for (Entry<String, String[]> set : params.entrySet())
		{
			if (set.getValue().length == 0 || StringUtils.isBlank(set.getValue()[0]))
			{
				continue;
			}
			if (!urlResult.contains("?"))
			{
				urlResult = urlResult + "?";
			}
			if (!urlResult.endsWith("&") && !urlResult.endsWith("?"))
			{
				urlResult = urlResult + "&";
			}
			urlResult += set.getKey() + "=" + set.getValue()[0];
		}
		return urlResult;
	}
	
	/**
	 * 根据字符串获取请求方法
	 * @param method
	 * @return
	 */
	public static HttpMethod getMethod(String method) {
		switch (method.toUpperCase()) {
		case "GET":
			return HttpMethod.GET;
		case "PUT":
			return HttpMethod.PUT;
		case "DELETE":
			return HttpMethod.DELETE;
		case "POST":
			return HttpMethod.POST;
		default:
			return HttpMethod.GET;
		}
	}
}
