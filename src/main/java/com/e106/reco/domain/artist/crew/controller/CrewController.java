package com.e106.reco.domain.artist.crew.controller;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.dto.CrewAcceptDto;
import com.e106.reco.domain.artist.crew.dto.CrewJoinDto;
import com.e106.reco.domain.artist.crew.service.CrewService;
import com.e106.reco.global.auth.dto.JoinDto;
import com.e106.reco.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/crew")
public class CrewController {
    private final CrewService crewService;

    @PostMapping
    public ResponseEntity<CommonResponse> createCrew(@RequestBody @Valid CreateDto createDto) {
        return ResponseEntity.ok(crewService.createCrew(createDto));
    }

    @PostMapping("/join")
    public ResponseEntity<CommonResponse> joinCrew(@RequestBody @Valid CrewJoinDto joinDto) {
        return ResponseEntity.ok(crewService.joinCrew(joinDto));
    }
    @PostMapping("/accept")
    public ResponseEntity<CommonResponse> acceptCrew(@RequestBody @Valid CrewAcceptDto acceptDto) {
        return ResponseEntity.ok(crewService.acceptCrew(acceptDto));
    }
}