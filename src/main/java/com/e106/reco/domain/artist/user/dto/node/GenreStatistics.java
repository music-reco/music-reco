package com.e106.reco.domain.artist.user.dto.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenreStatistics {
    private String genre;
    private long artistCount;
}
