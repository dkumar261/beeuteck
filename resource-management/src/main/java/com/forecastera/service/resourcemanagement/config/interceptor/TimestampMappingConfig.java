package com.forecastera.service.resourcemanagement.config.interceptor;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * @Author Uttam Kachhad
 * @Create 26-05-2023
 * @Description
 */
@Configuration
@NoArgsConstructor
public class TimestampMappingConfig {

    @Bean("requestProcessingTimeInterceptor")
    public MappedInterceptor mappedTenantInterceptor() {
        return new MappedInterceptor(new String[]{"/**"}, new RequestProcessingTimeInterceptor());
    }

}
