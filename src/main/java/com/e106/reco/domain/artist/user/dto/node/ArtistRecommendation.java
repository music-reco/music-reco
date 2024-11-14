package com.e106.reco.domain.artist.user.dto.node;

import com.e106.reco.domain.artist.user.node.ArtistNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ArtistRecommendation {
    private ArtistNode artistNode;
    private long genreOverlap;
    private List<String> sharedGenres;
}