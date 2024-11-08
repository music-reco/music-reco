package com.e106.reco.domain.workspace.dto.divide;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreviewResult {
    private Double duration;
    private String stem;

    @JsonProperty("stem_track")
    private String stemTrack;

    @JsonProperty("stem_track_size")
    private Long stemTrackSize;

    @JsonProperty("back_track")
    private String backTrack;

    @JsonProperty("back_track_size")
    private Long backTrackSize;
}