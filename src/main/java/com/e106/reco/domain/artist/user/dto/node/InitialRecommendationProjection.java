package com.e106.reco.domain.artist.user.dto.node;

import java.util.List;

public interface InitialRecommendationProjection {
    String getName();
    Long getArtistSeq();
    String getGenre();
    String getPosition();
    String getRegion();
    double getSimilarityScore();
}