package com.e106.reco.domain.workspace.dto.divide;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LimitsResponse {
    private String status;
    private String error;
    private String option;
    private String email;

    @JsonProperty("process_duration_limit")
    private Double processDurationLimit;

    @JsonProperty("process_duration_used")
    private Double processDurationUsed;

    @JsonProperty("process_duration_left")
    private Double processDurationLeft;
}
