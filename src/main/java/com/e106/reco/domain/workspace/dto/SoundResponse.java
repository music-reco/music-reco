package com.e106.reco.domain.workspace.dto;

import com.e106.reco.domain.workspace.entity.converter.SoundType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoundResponse {
    private Long soundSeq;
    private Double startPoint;
    private Double endPoint;
    private String url;
    private SoundType type;
}
