package com.e106.reco.domain.artist.user.dto.node;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InitialRecommendationDTO {
    private String name;
    private String nickname;
    private Long artistSeq;
    private String genre;
    private String position;
    private String profileImage;
    private String region;
    private double similarityScore;  // 0.0 ~ 1.0 정규화된 점수
}