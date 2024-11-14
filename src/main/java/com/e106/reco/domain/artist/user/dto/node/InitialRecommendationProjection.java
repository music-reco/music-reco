package com.e106.reco.domain.artist.user.dto.node;

import java.util.List;

public interface InitialRecommendationProjection {
    String getArtistName();
    double getSimilarityScore();
    List<String> getSharedGenres();
    List<String> getSharedInstruments();
    String getArtistRegion();
}
