package com.dongsw.authority.aop;

import com.dongsw.authority.common.exception.RespExcption;
import com.dongsw.authority.common.json.JsonResult;
import com.dongsw.authority.common.json.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 过滤web请求,并处理自定义断言异常
 *
 * @author 戚羿辰
 * @desc
 * @Date 2017/01/12
 */
@Component
@Aspect
public class WebControllerAspect {

    private static final Logger logger = LoggerFactory.getLogger(WebControllerAspect.class);

    @Around("execution(* com.dongsw.authority.controller.*Controller.*(..))")
    public Object doControllerAround(ProceedingJoinPoint pjp) {
        JsonResult jsonResult ;
        Object result ;
        // 是否返回json
        try {
            result = pjp.proceed();
        } catch (RespExcption e) {
            logger.error("业务异常", e);
            // 断言时抛出的错误
            jsonResult = new JsonResult(null, e.getMessage(), e.getCode());
            result = JsonUtil.toJson(jsonResult);
        }catch (Throwable e) {
            // 程序内部错误, 交给 Whitelabel Error Page 处理
            logger.error(e.getMessage(),e);
            throw new RespExcption();
        }
        return result;
    }

}
