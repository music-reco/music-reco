package com.e106.reco.domain.artist.user.controller;

import com.e106.reco.domain.artist.user.dto.node.ArtistRecommendationDTO;
import com.e106.reco.domain.artist.user.dto.node.InitialRecommendationDTO;
import com.e106.reco.domain.artist.user.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping("/{artistSeq}")
    public ResponseEntity<List<ArtistRecommendationDTO>> getRecommendations(
            @PathVariable Long artistSeq,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(
                recommendService.getRecommendations(artistSeq, limit)
        );
    }

    @GetMapping("/{artistSeq}/initial")
    public ResponseEntity<List<InitialRecommendationDTO>> getInitialRecommendations(
            @PathVariable Long artistSeq,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(recommendService.getInitialRecommendations(artistSeq, limit));
    }
}