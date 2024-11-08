package com.e106.reco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class RecoApplication {

    public static void main(String[] args) {

        SpringApplication.run(RecoApplication.class, args);
    }

}
