package com.e106.reco.domain.artist.user.dto.node;

import java.util.List;

// 추천 결과를 받기 위한 Projection 인터페이스
public interface ArtistRecommendationProjection {
    String getArtistName();
    double getSimilarityScore();
    List<String> getSharedGenres();
    List<String> getSharedInstruments();
    List<String> getSharedRegions();
}
