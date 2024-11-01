package com.e106.reco.domain.artist.crew.controller;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.dto.CrewChangeDto;
import com.e106.reco.domain.artist.crew.dto.CrewDto;
import com.e106.reco.domain.artist.crew.dto.CrewGrantDto;
import com.e106.reco.domain.artist.crew.dto.CrewRoleDto;
import com.e106.reco.domain.artist.crew.service.CrewService;
import com.e106.reco.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<CommonResponse> joinCrew(@RequestBody @Valid CrewDto joinDto) {
        return ResponseEntity.ok(crewService.joinCrew(joinDto));
    }

    @PatchMapping("/accept")
    public ResponseEntity<CommonResponse> acceptCrew(@RequestBody @Valid CrewChangeDto acceptDto) {
        return ResponseEntity.ok(crewService.acceptCrew(acceptDto));
    }

    @DeleteMapping("/decline")
    public ResponseEntity<CommonResponse> declineCrew(@RequestBody @Valid CrewChangeDto declineDto) {
        return ResponseEntity.ok(crewService.declineCrew(declineDto));
    }

    @DeleteMapping("/leave")
    public ResponseEntity<CommonResponse> leaveCrew(@RequestBody @Valid CrewDto leaveDto) {
        return ResponseEntity.ok(crewService.leaveCrew(leaveDto));
    }

    @PutMapping("/grant")
    public ResponseEntity<List<CrewRoleDto>> grantCrew(@RequestBody @Valid CrewGrantDto crewGrantDto){
        return ResponseEntity.ok(crewService.grantCrew(crewGrantDto));
    }

    @GetMapping("/grant/{crewSeq}")
    public ResponseEntity<List<CrewRoleDto>> roleCrew(@PathVariable Long crewSeq){
        return ResponseEntity.ok(crewService.roleCrew(crewSeq));
    }

}