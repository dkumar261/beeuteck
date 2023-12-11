package com.forecastera.service.financialmanagement.commonResponseUtil;

import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Uttam Kachhad
 * @Create 26-05-2023
 * @Description
 */
@NoArgsConstructor
public class ResponseUtil {

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
