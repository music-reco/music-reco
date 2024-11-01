package com.e106.reco.domain.artist.entity;

import com.e106.reco.global.error.errorcode.ArtistErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Position {
    VOCAL("보컬"), BASE("베이스"), DRUM("드럼"), ELECT("일렉트릭기타"), ACUST("어쿠스틱기타"), DISIT("건반"),
    STRING("현악기"), WIND("관악기"), PIANO("피아노"), CREW("크루");

    private final String name;

    @JsonCreator
    public static Position of(String inputValue) {
        return Stream.of(Position.values())
                .filter(position -> position.name.equals(inputValue))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ArtistErrorCode.POSITION));

    }
}
