package com.e106.reco.global.config;

import com.e106.reco.domain.chat.entity.ChatArtist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword(password);
        log.info("host : {}", host);
        log.info("port : {}", port);
        log.info("password : {}", password);
        return new LettuceConnectionFactory(config);
    }
    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword(password);
        return new LettuceConnectionFactory(config);
    }
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveStringRedisTemplate() {
        ReactiveRedisConnectionFactory rrcf = reactiveRedisConnectionFactory();

        // String 타입에는 Jackson2JsonRedisSerializer를 사용하지 않고 StringRedisSerializer 사용
        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, String> context = builder.value(new StringRedisSerializer())
                .hashValue(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .build();

        return new ReactiveRedisTemplate<>(rrcf, context);
    }
    @Bean
    public ReactiveRedisTemplate<String, ChatArtist> artistRedisTemplate() {
        ReactiveRedisConnectionFactory rrcf = reactiveRedisConnectionFactory();

        Jackson2JsonRedisSerializer<ChatArtist> serializer =
                new Jackson2JsonRedisSerializer<>(ChatArtist.class);

        // String 타입에는 StringRedisSerializer 사용
        RedisSerializationContext.RedisSerializationContextBuilder<String, ChatArtist> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, ChatArtist> context = builder
                .value(serializer)  // ChatArtist 객체 직렬화 설정
                .hashValue(serializer)
                .hashKey(new StringRedisSerializer())  // key는 String으로 설정
                .build();
        return new ReactiveRedisTemplate<>(rrcf, context);
    }

//    @Bean
//    public ReactiveRedisTemplate<String, ChatArtistState> artistStateRedisTemplate() {
//        ReactiveRedisConnectionFactory rrcf = reactiveRedisConnectionFactory();
//
//        Jackson2JsonRedisSerializer<ChatArtistState> serializer =
//                new Jackson2JsonRedisSerializer<>(ChatArtistState.class);
//
//        // String 타입에는 StringRedisSerializer 사용
//        RedisSerializationContext.RedisSerializationContextBuilder<String, ChatArtistState> builder =
//                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
//
//        RedisSerializationContext<String, ChatArtistState> context = builder
//                .value(serializer)  // ChatArtist 객체 직렬화 설정
//                .hashValue(serializer)
//                .hashKey(new StringRedisSerializer())  // key는 String으로 설정
//                .build();
//        return new ReactiveRedisTemplate<>(rrcf, context);
//    }
}
