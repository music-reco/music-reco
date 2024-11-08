package com.e106.reco.domain.workspace.dto.divide;

import lombok.Data;

@Data
public class TaskResult {
    private String state;
    private String error;
    private Integer progress;
}