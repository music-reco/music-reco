package com.e106.reco.domain.artist.crew.dto;

import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import com.e106.reco.domain.artist.user.dto.UserSummaryDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CrewRoleDto {
    UserSummaryDto user;
    CrewUserState state;
}