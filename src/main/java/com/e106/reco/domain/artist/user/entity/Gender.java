package com.e106.reco.domain.artist.user.entity;

import com.e106.reco.global.error.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

import static com.e106.reco.global.error.errorcode.ArtistErrorCode.GENDER;


@RequiredArgsConstructor
@Getter
public enum Gender {
    FEMALE("여성"), MALE("남성"), ETC("기타");

    private final String name;

    @JsonCreator
    public static Gender of(String inputValue) {
        return Stream.of(Gender.values())
                .filter(gender -> gender.name.equals(inputValue))
                .findFirst()
                .orElseThrow(() -> new BusinessException(GENDER));
    }
}
