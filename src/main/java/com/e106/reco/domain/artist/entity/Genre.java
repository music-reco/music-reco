package com.e106.reco.domain.artist.entity;

import com.e106.reco.global.error.errorcode.ArtistErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Genre {
    ROCK("락"), BALAD("발라드"), INDI("인디"), DANCE( "댄스"), CLASSIC("클래식"), JAZ("재즈"), ORCHEATRA("오케스트라"), RAP("랩"), ETC("기타");

    private final String name;

    @JsonCreator
    public static Genre of(String inputValue) {
        return Arrays.stream(Genre.values())
                .filter(genre -> genre.name.equals(inputValue))
                .findAny()
                .orElseThrow(() -> new BusinessException(ArtistErrorCode.GENRE));
    }

}
