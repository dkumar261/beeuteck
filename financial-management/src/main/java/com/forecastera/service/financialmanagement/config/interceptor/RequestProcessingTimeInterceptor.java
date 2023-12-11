package com.forecastera.service.financialmanagement.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Uttam Kachhad
 * @Create 26-05-2023
 * @Description
 */
public class RequestProcessingTimeInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestProcessingTimeInterceptor.class);

    public RequestProcessingTimeInterceptor() {
    }

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.trace("Request URL::" + httpServletRequest.getRequestURL().toString() + ":: Start Time=" + System.currentTimeMillis() + ":" + httpServletRequest.getAttribute("startTime"));


        // Allow Request From Gateway Application Only
        // Allow Swagger To Access This Application
        /*if(StringUtils.isEmpty(httpServletRequest.getHeader("origin")) || !httpServletRequest.getHeader("origin").equals(ProjectUtils.GATEWAY_URL)){
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }*/
        /*if ((!httpServletRequest.getRequestURL().toString().startsWith(ProjectUtils.GATEWAY_URL))
        && (!httpServletRequest.getRequestURL().toString().startsWith(ProjectUtils.SWAGGER_URL))) {
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }*/

        long startTime = System.currentTimeMillis();
        if (httpServletRequest.getAttribute("startTime") == null) {
            httpServletRequest.setAttribute("startTime", startTime);
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
