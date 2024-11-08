package com.e106.reco.domain.workspace.dto.divide;

import lombok.Data;

@Data
public class FileResult {
    private String status;
    private String name;
    private Long size;
    private Double duration;
    private String splitter;
    private String stem;
    private SplitResult split;
    private TaskResult task;
    private String error;
}