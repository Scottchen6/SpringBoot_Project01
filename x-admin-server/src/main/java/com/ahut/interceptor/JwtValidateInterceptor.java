package com.ahut.interceptor;

import com.ahut.utils.JwtUtil;
import com.ahut.vo.Result;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author : Scott Chen
 * @create 2023/6/16 21:30
 */

@Component
@Slf4j
public class JwtValidateInterceptor  implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    //在controller执行之前先拦截，之后的操作不做拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI()  + "待验证" + token);
        if(token !=null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "放行。。。。。");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "禁止访问！");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(Result.fail(20003,"jwt令牌无效，请重新登录！")));
        return false;
    }
}
