package com.e106.reco.domain.artist.user.dto.node;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InitialRecommendationDTO {
    private String name;
    private double similarityScore;  // 0.0 ~ 1.0 정규화된 점수
    private String recommendationReason;
    private Map<String, Object> matchDetails;
}