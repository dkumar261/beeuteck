package com.forecastera.service.usersettingsmanagement.config.cors;

import com.netflix.appinfo.AmazonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Uttam Kachhad
 * @Create 29-05-2023
 * @Description This filter used to CORS error for swagger
 */
@Configuration
public class CorsFilter implements WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(CorsFilter.class);

    @Value("${userSettings.allowOrigin}")
    private String allowOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(allowOrigin).allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE").allowedHeaders("*").exposedHeaders("*");
    }

    @Bean
    @Profile("!local")
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
        AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
        b.setDataCenterInfo(info);
        return b;
    }

}
