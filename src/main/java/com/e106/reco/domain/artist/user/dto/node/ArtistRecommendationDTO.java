package com.e106.reco.domain.artist.user.dto.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ArtistRecommendationDTO {
    private String artistName;
    private double similarity;  // 0.0 ~ 1.0
    private String recommendationReason;
    private List<String> sharedGenres;
    private List<String> sharedInstruments;
    private List<String> sharedRegions;
}
