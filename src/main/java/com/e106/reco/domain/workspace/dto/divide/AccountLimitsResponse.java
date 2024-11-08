package com.e106.reco.domain.workspace.dto.divide;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountLimitsResponse {
    private String option;
    private String email;
    private Double totalDurationLimit;
    private Double usedDuration;
    private Double remainingDuration;
}
