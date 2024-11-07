package com.e106.reco.domain.artist.entity;

import com.e106.reco.global.error.errorcode.ArtistErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Region {
    SU("서울"), BS("부산"), DG("대구"), GJ("광주"), DJ("대전"), US("울산"), IC("인천"), JJ("제주"),
    GW_S("강원도남부"), GW_N("강원도북부"), KK_S("경기도남부"), KK_N("경기도북부"), KS("경상남도"), KN("경상북도"), CS("충청남도"), CN("충청북도"), JS("전라남도"), JN("전라북도");

    private final String name;

    @JsonCreator
    public static Region of(String inputValue) {
        return Stream.of(Region.values())
                .filter(region -> region.name.equals(inputValue))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ArtistErrorCode.REGION));

    }
}
