package com.e106.reco.domain.artist.crew.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CrewDto {
    @NotNull
    private Long crewSeq;
}
