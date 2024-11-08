package com.e106.reco.domain.workspace.dto.divide;

import lombok.Data;

import java.util.Map;

@Data
public class CheckResponse {
    private String status;
    private Map<String, FileResult> result;
    private String error;
}
