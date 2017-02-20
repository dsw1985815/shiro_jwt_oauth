/**
 * Comments：
 * Author：dongshuangwei
 * Create Date：2017/2/16
 * Modified By：
 * Modified Date：
 * Why & What is modified：
 * Version：v1.0
 */
package com.quheng.usercenter.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quheng.usercenter.config.Audience;
import com.quheng.usercenter.jwt.JwtHelper;
import com.quheng.usercenter.utils.ResultMsg;
import com.quheng.usercenter.utils.ResultStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HTTPBearerAuthorizeAttribute implements Filter {
    @Autowired
    private Audience audienceEntity;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Token验证
        ResultMsg resultMsg;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 从请求头中获取到token
        String auth = httpRequest.getHeader("Authorization");
        if ((auth != null) && (auth.length() > 7)) {
            //截取token类型，区分类型进行不同方式处理
            String HeadStr = auth.substring(0, 6).toLowerCase();
            //时间段类型
            if (HeadStr.compareTo("intevl") == 0) {

            //一次性
            }else if(HeadStr.compareTo("onetim") == 0){

            }
            auth = auth.substring(7, auth.length());
            if (checkToken(auth)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();

        resultMsg = new ResultMsg(ResultStatusCode.INVALID_TOKEN.getErrcode(), ResultStatusCode.INVALID_TOKEN.getErrmsg(), null);
        httpResponse.getWriter().write(mapper.writeValueAsString(resultMsg));
        return;
    }

    private boolean checkToken(String auth){
        return JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret()) != null;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }
}