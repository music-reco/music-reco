package com.e106.reco.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        // CORS 설정
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 자격 증명 허용
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인 추가
        config.addAllowedOrigin("https://k11e106.p.ssafy.io");
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.setExposedHeaders(Arrays.asList("Authorization")); // 노출할 헤더 설정
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // 허용할 HTTP 메서드

        // ReactiveCorsConfigurationSource 구현
        CorsConfigurationSource source = new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
                return Mono.just(config).block(); // CORS 설정을 Mono로 반환
            }
        };

        return new CorsWebFilter(source);  // WebFlux에서 사용할 CorsWebFilter 반환
    }
}
