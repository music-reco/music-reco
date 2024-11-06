package com.e106.reco.domain.workspace.dto.midify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyPoint {
    private Long soundSeq;
    private Double startPoint;
    private Double endPoint;
}
