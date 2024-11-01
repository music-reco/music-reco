package com.e106.reco.domain.artist.crew.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CrewJoinDto {
    @NotNull
    private Long crewSeq;
}
