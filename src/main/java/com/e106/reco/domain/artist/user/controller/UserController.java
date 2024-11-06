package com.e106.reco.domain.artist.user.controller;

import com.e106.reco.domain.artist.user.dto.UserInfoDto;
import com.e106.reco.domain.artist.user.service.UserService;
import com.e106.reco.global.auth.dto.JoinDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoDto> userInfo() {
        return ResponseEntity.ok(userService.getInfo());
    }

    @PostMapping
    public ResponseEntity<UserInfoDto> userInfo(@RequestPart @Valid JoinDto userDto, @RequestParam(value = "profile", required = false) MultipartFile file) {
        return ResponseEntity.ok(userService.updateInfo(userDto, file));
    }
}
