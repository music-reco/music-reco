package com.e106.reco.global.config;

import com.e106.reco.global.auth.jwt.WebfluxJwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@RequiredArgsConstructor
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {
    private final WebfluxJwtFilter webfluxJwtFilter;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> {})
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) //session STATELESS
                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("*").permitAll() // WebSocket 연결 허용
//                        .pathMatchers("/api/auth/**", "/error").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(webfluxJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
