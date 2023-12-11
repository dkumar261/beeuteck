package com.forecastera.service.gateway.controller;

import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author Uttam Kachhad
 * @Create 22-05-2023
 * @Description
 */
@RestController
public class SwaggerController {

    // Services to exclude. You can modify this list as per your environment
    private final DiscoveryClient discoveryClient;
    @Autowired
    private Environment environment;
    private String activeEnvironment = "LOCAL";

    @Value("${gateway.url}")
    private String gatewayURL;

    public SwaggerController(final DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/v3/api-docs/swagger-config")
    public Map<String, Object> swaggerConfig(ServerHttpRequest serverHttpRequest) throws URISyntaxException {

        String[] currentEnvironment = environment.getActiveProfiles();

        if (currentEnvironment.length > 0) {
            activeEnvironment = currentEnvironment[0].toUpperCase();
        }

        URI uri = serverHttpRequest.getURI();
        String url = new URI(uri.getScheme(), uri.getAuthority(), null, null, null).toString();
        Map<String, Object> swaggerConfig = new LinkedHashMap<>();
        List<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls = new LinkedList<>();
        System.out.println("Services = " + discoveryClient.getServices());

        discoveryClient.getServices().forEach(serviceName -> {
                    if (!serviceName.equals("gateway-dev") &&
                            !serviceName.equals("eureka-server-dev") &&
                            !serviceName.equals("gateway-local") &&
                            !serviceName.equals("eureka-server-local") &&
                            !serviceName.equals("gateway-stage") &&
                            !serviceName.equals("eureka-server-stage")
                    ) {
                        discoveryClient.getInstances(serviceName).forEach(serviceInstance ->
                                {

                                    swaggerUrls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(serviceName, gatewayURL + "" + serviceInstance.getPort() + "/v3/api-docs", serviceName.toUpperCase()));
                                }
                        );
                    }
                }
        );
        swaggerConfig.put("urls", swaggerUrls);
        return swaggerConfig;
    }
}
