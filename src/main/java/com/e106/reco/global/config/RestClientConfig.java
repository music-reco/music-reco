package com.e106.reco.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private static final String BASE_URL = "https://www.lalal.ai";

    @Value("${spring.lalal.ai.license-key}")
    private String licenseKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("Authorization", "license " + licenseKey)
                .build();
    }

    @Bean
    public ObjectMapper ObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //pretty print json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        //value -> null 무시
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //LocalDate, LocalDateTime support jsr310
        objectMapper.registerModule(new JavaTimeModule());
        //timestamp 출력을 disable. -> 문자열형태로 출력
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
