package com.e106.reco.domain.workspace.dto.divide;

import lombok.Data;

@Data
public class ArchiveResult {
    private String status;
    private String url;
    private Long size;
    private String error;
}