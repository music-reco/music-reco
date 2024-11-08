package com.e106.reco.domain.workspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequest {
    @NotNull
    private String name;
    @NotNull
    private String originSinger;
    @NotNull
    private String originTitle;
}
