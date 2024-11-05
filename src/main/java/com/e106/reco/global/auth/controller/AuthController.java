package com.e106.reco.global.auth.controller;

import com.e106.reco.domain.artist.user.controller.UserController;
import com.e106.reco.global.auth.dto.JoinDto;
import com.e106.reco.global.auth.dto.MailDto;
import com.e106.reco.global.auth.service.AuthService;
import com.e106.reco.global.auth.token.service.TokenService;
import com.e106.reco.global.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;
    private final UserController userController;

    @PostMapping("/join")
    public ResponseEntity<CommonResponse> join(@RequestPart @Valid JoinDto joinDto,
                                               @RequestParam(value = "profile", required = false) MultipartFile file){
        return ResponseEntity.ok(authService.join(joinDto, file));
    }

    @PostMapping("/email")
    public ResponseEntity<CommonResponse> sendEmail(@RequestBody @Valid MailDto mailDto){
        return ResponseEntity.ok(authService.sendEmail(mailDto));
    }

    @PostMapping("/email/code")
    public ResponseEntity<CommonResponse> verifyEmailCode(@RequestBody @Valid MailDto mailDto){
        return ResponseEntity.ok(authService.verifyEmailCode(mailDto));
    }


    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        tokenService.reissueProcess(request, response);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
