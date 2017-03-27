package com.dongsw.authority.common.model;

import com.dongsw.authority.aop.WebControllerAspect;
import com.dongsw.authority.common.def.ResultCode;
import com.dongsw.authority.common.exception.RespExcption;
import com.dongsw.authority.common.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 重写RestTemplate, 处理发送请求后遇到异常不返回具体信息的问题
 *
 * @author 戚羿辰
 * @desc
 * @Date 2017/01/11
 */
public class CustomRestTemplate extends RestTemplate {

    private static final Logger log = LoggerFactory.getLogger(WebControllerAspect.class);

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... urlVariables) {
        RequestCallback requestCallback = acceptHeaderRequestCallback(responseType);
        HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType,
                getMessageConverters(), logger);
        URI expanded = getUriTemplateHandler().expand(url, urlVariables);

        return doExecute(expanded, HttpMethod.GET, requestCallback, responseExtractor);
    }

    @Override
    public <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
                           ResponseExtractor<T> responseExtractor) {
        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            if (responseExtractor != null) {
                T result = responseExtractor.extractData(response);

                // 处理远程调用抛出的异常,如果httpcode不是正常状态,并且存在可以解析的response, 则将该异常抛出至aop处理
                handleResponse(response, result);

                return result;
            } else {
                return null;
            }
        } catch (IOException ex) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = query != null ? resource.substring(0, resource.indexOf(query) - 1) : resource;
            throw new ResourceAccessException(
                    "I/O error on " + method.name() + " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * 处理远程调用抛出的异常,如果httpcode不是正常状态,并且存在可以解析的response,
     * 则将该异常抛出至aop处理,否则调用原生方法抛出错误
     *
     * @param response
     * @param result
     * @throws IOException
     * @throws
     * @author 戚羿辰
     * @Date 2017/01/12
     */
    @SuppressWarnings("unchecked")
    private <T> void handleResponse(ClientHttpResponse response, T result) throws IOException {
        Map<String, Object> map = null;
        ResponseErrorHandler errorHandler = getErrorHandler();
        boolean hasError = errorHandler.hasError(response);
        // 如果返回正常则不进行解析
        if (!hasError) {
            return;
        }

        try {
            // 尝试不同类型的转换(String, map, ResponseEntity).后续遇到非预期的类型需要增加判断
            map = JsonUtil.toObject(result.toString(), Map.class);
            if (result.getClass().equals(ResponseEntity.class)) { // 尝试转换为ResponseEntity
                ResponseEntity<T> resp = (ResponseEntity<T>) result;
                //  根据ResponseEntity的body类型转换
                if (String.class.equals(resp.getBody().getClass())) {
                    map = JsonUtil.toObject(resp.getBody().toString(), Map.class);
                } else {
                    map = (Map<String, Object>) resp.getBody();
                }
            } else if (result.getClass().equals(LinkedHashMap.class)) { // 转换map
                map = (Map<String, Object>) result;
            }
        } catch (Exception e) {
            log.error("解析失败", e);
            // 解析失败则交给原方法处理
            errorHandler.handleError(response);
        }
        if (map != null && null != map.get("message") && !"No message available".equals(map.get("message"))) {
            throw new RespExcption(ResultCode.FAILED, map.get("message").toString());
        } else {
            // 返回信息不存在则抛给原类处理
            errorHandler.handleError(response);
        }
    }

}