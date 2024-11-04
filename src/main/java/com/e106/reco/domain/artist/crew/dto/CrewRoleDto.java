package com.e106.reco.domain.artist.crew.dto;

import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CrewRoleDto {
    Long userSeq;
    CrewUserState state;
}