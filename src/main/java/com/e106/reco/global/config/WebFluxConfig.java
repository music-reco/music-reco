//package com.e106.reco.global.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ReactorResourceFactory;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.web.reactive.config.WebFluxConfigurer;
//import reactor.netty.resources.ConnectionProvider;
//
//import java.time.Duration;
//
//@Configuration
//public class WebFluxConfig implements WebFluxConfigurer {
//
//    @Override
//    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//        configurer.defaultCodecs().maxInMemorySize(1024 * 1024); // 1MB
//    }
//
//    @Bean
//    public ConnectionProvider connectionProvider() {
//        return ConnectionProvider.builder("custom")
//                .maxConnections(500)
//                .pendingAcquireTimeout(Duration.ofSeconds(60))
//                .maxIdleTime(Duration.ofSeconds(60))
//                .build();
//    }
//
//    @Bean
//    public ReactorResourceFactory reactorResourceFactory() {
//        ReactorResourceFactory factory = new ReactorResourceFactory();
//        factory.setUseGlobalResources(false);
//        factory.setConnectionProvider(connectionProvider());
//        return factory;
//    }
//}
