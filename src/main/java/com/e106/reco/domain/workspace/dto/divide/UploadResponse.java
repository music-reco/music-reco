package com.e106.reco.domain.workspace.dto.divide;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UploadResponse {
    private String status;
    private String id;
    private Double duration;
    private Integer size;
    private Integer expires;
    private String error;
}