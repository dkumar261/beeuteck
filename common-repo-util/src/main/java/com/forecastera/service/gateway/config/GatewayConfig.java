package com.forecastera.service.gateway.config;


/*import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.converters.wrappers.CodecWrappers;
import com.netflix.discovery.provider.DiscoveryJerseyProvider;
import com.netflix.discovery.shared.MonitoredConnectionManager;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import com.netflix.discovery.shared.transport.jersey.SSLSocketFactoryAdapter;
import com.sun.deploy.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.apache4.config.ApacheHttpClient4Config;
import io.netty.util.internal.ResourcesUtil;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;
import java.security.KeyStore;*/
import com.netflix.appinfo.AmazonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @Author Uttam Kachhad
 * @Create 18-05-2023
 * @Description
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private Environment environment;

    private String activeEnvironment = "DEV";

    @Value("${gateway.url}")
    private String gatewayURL;

//    @Autowired
//    EurekaClientConfig config;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {

        String[] currentEnvironment = environment.getActiveProfiles();
        if (currentEnvironment.length > 0) {
            activeEnvironment = currentEnvironment[0].toUpperCase();
        }

        return builder.routes()
                .route("PROJECT-MANAGEMENT-" + activeEnvironment, r -> r.path("/project/**")
                        .uri(gatewayURL + "8003"))

                .route("RESOURCE-MANAGEMENT-" + activeEnvironment, r -> r.path("/resource/**")
                        .uri(gatewayURL + "8004"))

                .route("FINANCIAL-MANAGEMENT-" + activeEnvironment, r -> r.path("/financial/**")
                        .uri(gatewayURL + "8005"))

                .route("USER-SETTING-MANAGEMENT-" + activeEnvironment, r -> r.path("/user-setting/**")
                        .uri(gatewayURL + "8002"))

                .build();
    }

    @Bean
    @Profile("!local")
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
        AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
        b.setDataCenterInfo(info);
        return b;
    }

    /*@Bean
    public DiscoveryClient.DiscoveryClientOptionalArgs getTrustStoredEurekaClient()
            throws Exception {
        final KeyStore trustStore = KeyStore.getInstance("jks");
        trustStore.load(new ClassPathResource("cacerts.jks").getInputStream(), "changeit".toCharArray());

        SSLConnectionSocketFactory systemSocketFactory = new SSLConnectionSocketFactory(
                SSLContexts
                        .custom()
                        .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                        .build(),
                new NoopHostnameVerifier());

        SchemeRegistry sslSchemeRegistry = new SchemeRegistry();
        Scheme schema = new Scheme("https", 8001, new SSLSocketFactoryAdapter(systemSocketFactory));
        sslSchemeRegistry.register(schema);
        String name = "Custom-Discovery-Client";
        MonitoredConnectionManager connectionManager = new MonitoredConnectionManager(name, sslSchemeRegistry);
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getProperties().put(ApacheHttpClient4Config.PROPERTY_CONNECTION_MANAGER, connectionManager);

        DiscoveryJerseyProvider discoveryJerseyProvider = new DiscoveryJerseyProvider(
                CodecWrappers.getEncoder(config.getEncoderName()),
                CodecWrappers.resolveDecoder(config.getDecoderName(), config.getClientDataAccept()));

        clientConfig.getSingletons().add(discoveryJerseyProvider);

        DiscoveryClient.DiscoveryClientOptionalArgs clientOptionalArgs = new DiscoveryClient.DiscoveryClientOptionalArgs();
        clientOptionalArgs.setEurekaJerseyClient(new EurekaJerseyClientImpl(
                config.getEurekaServerConnectTimeoutSeconds() * 1000,
                config.getEurekaServerReadTimeoutSeconds() * 1000,
                config.getEurekaConnectionIdleTimeoutSeconds() * 1000,
                clientConfig));
        return clientOptionalArgs;
    }*/

}
