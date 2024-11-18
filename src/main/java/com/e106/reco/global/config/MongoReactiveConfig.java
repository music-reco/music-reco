//package com.e106.reco.global.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//
//@Configuration
//public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {
//
//    @Value("${spring.data.mongodb.uri}")
//    String uri;
//
//    @Value("${spring.data.mongodb.database}")
//    String databaseName; // 설정 파일에서 데이터베이스 이름을 가져옵니다.
//
//    @Override
//    protected String getDatabaseName() {
//        // 이미 존재하는 데이터베이스 이름을 반환
//        return databaseName != null ? databaseName : "RECO";  // 기본 값은 "reco"
//    }
//
//    @Override
//    @Bean
//    public MongoClient reactiveMongoClient() {
//        return MongoClients.create(uri);
//    }
//
//    @Bean
//    public ReactiveMongoTemplate reactiveMongoTemplate() {
//        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
//    }
//}
