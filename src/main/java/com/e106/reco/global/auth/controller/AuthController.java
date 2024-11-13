package com.e106.reco.global.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController{
    @PostMapping("/login")
    public Mono<String> login(@RequestParam String username, @RequestParam String password) {
        // 로그인 처리 로직 (예: 토큰 발급)
        return Mono.just("Login Successful for " + username);
    }
}