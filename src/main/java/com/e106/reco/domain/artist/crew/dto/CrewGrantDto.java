package com.e106.reco.domain.artist.crew.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class CrewGrantDto {
    Long crewSeq;
    List<CrewRoleDto> crews;
}
