package com.e106.reco.domain.workspace.dto.divide;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SplitParams {
    private String id;
    private String splitter;
    private String stem;

    @JsonProperty("dereverb_enabled")
    @Builder.Default
    private boolean dereverbEnabled = false;

    @JsonProperty("enhanced_processing_enabled")
    @Builder.Default
    private boolean enhancedProcessingEnabled = true;

    @JsonProperty("noise_cancelling_level")
    @Builder.Default
    private int noiseCancellingLevel = 1;

    public static SplitParams createDefault(String stem) {
        return SplitParams.builder()
                .stem(stem)
                .build();
    }
}