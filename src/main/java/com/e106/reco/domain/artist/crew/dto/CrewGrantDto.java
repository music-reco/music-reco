package com.e106.reco.domain.artist.crew.dto;

import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class CrewGrantDto {
    List<UserCrewDto> crews;

    @Getter
    @RequiredArgsConstructor
    static public class UserCrewDto {
        Long userSeq;
        CrewUserState state;
    }
}
