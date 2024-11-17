package com.e106.reco.global.config;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class Neo4jConfig {

    @Bean
    public TransactionTemplate neo4jTransactionTemplate(Neo4jTransactionManager neo4jTransactionManager) {
        return new TransactionTemplate(neo4jTransactionManager);
    }

    @Bean
    public Neo4jTransactionManager neo4jTransactionManager(Driver driver) {
        return new Neo4jTransactionManager(driver);
    }
}