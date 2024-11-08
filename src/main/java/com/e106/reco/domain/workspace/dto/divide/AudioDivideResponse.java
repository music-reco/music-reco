package com.e106.reco.domain.workspace.dto.divide;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AudioDivideResponse {
    private String fileName;
    private Double duration;
    private String stemTrackUrl;
    private Long stemTrackSize;
    private String backTrackUrl;
    private Long backTrackSize;
}