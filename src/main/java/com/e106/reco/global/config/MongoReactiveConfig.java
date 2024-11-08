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
//    @Value("${spring.data.mongodb.uri}")
//    String uri;
//    @Value("${s}")
//
//    @Override
//    protected String getDatabaseName() {
//        return "mydatabase";
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