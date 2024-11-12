package com.e106.reco.domain.workspace.controller;

import com.e106.reco.domain.workspace.dto.divide.AccountLimitsResponse;
import com.e106.reco.domain.workspace.dto.divide.ApiResponse;
import com.e106.reco.domain.workspace.dto.divide.AudioDivideResponse;
import com.e106.reco.domain.workspace.service.DivideService;
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

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AudioDivideController {
    private final DivideService audioDivideService;

    @PostMapping("/workspace/divide")
    public ResponseEntity<ApiResponse<AudioDivideResponse>> divideAudio(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "stem") String stem,
            @RequestParam(value = "splitter", defaultValue = "phoenix") String splitter){
        log.info("stem : {}", stem);
        log.info("splitter : {}", splitter);
        log.info("Received audio divide request - File: {}, Stem: {}, Splitter: {}",
                file.getOriginalFilename(), stem, splitter);
        try {
            AudioDivideResponse response = audioDivideService.divideAudioFile(file, stem, splitter);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException e) {
            log.error("Failed to divide audio file", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/limits")
    public ResponseEntity<ApiResponse<AccountLimitsResponse>> getAccountLimits() {
        try {
            AccountLimitsResponse limits = audioDivideService.getAccountLimits();
            return ResponseEntity.ok(ApiResponse.success(limits));
        } catch (Exception e) {
            log.error("Failed to fetch account limits", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}