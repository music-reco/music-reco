package com.e106.reco.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor1() {
        ThreadPoolTaskExecutor temp = new ThreadPoolTaskExecutor();
        temp.setCorePoolSize(15);
        temp.setThreadNamePrefix("exec1-");
        temp.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(temp);
    }

    @Bean
    public Executor asyncExecutor2() {
        ThreadPoolTaskExecutor temp = new ThreadPoolTaskExecutor();
        temp.setCorePoolSize(15);
        temp.setThreadNamePrefix("exec2-");
        temp.initialize();
        return new DelegatingSecurityContextAsyncTaskExecutor(temp);
    }
}