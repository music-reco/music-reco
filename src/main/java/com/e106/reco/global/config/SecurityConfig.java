package com.e106.reco.global.config;

import com.e106.reco.global.auth.jwt.JwtFilter;
import com.e106.reco.global.auth.jwt.JwtUtil;
import com.e106.reco.global.auth.jwt.LoginFilter;
import com.e106.reco.global.auth.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final CorsFilter corsFilter;

    @Bean
    public static AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**"
                        ,"/actuator/**", "/images/**", "/js/**", "/css/**", "/ws/**");
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //해쉬를 암호화해 진행하기 위해서 필요한 것들

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        return http.cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .with(new Custom(authenticationManager(authenticationConfiguration), jwtUtil, tokenRepository), Custom::getClass)
                .build();
    }

    @RequiredArgsConstructor
    public static class Custom extends AbstractHttpConfigurer<Custom, HttpSecurity> {
        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;
        private final TokenRepository tokenRepository;

        @Override
        public void configure(HttpSecurity http) {
            LoginFilter loginFilter = new LoginFilter(authenticationManager, jwtUtil, tokenRepository);
            loginFilter.setFilterProcessesUrl("/api/auth/login");
            loginFilter.setPostOnly(true);
            http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}