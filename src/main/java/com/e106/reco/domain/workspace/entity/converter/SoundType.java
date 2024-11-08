package com.e106.reco.domain.workspace.entity.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SoundType {
    PIANO("피아노"),
    VOCAL("보컬"),
    ACOUSTIC_GUITAR("어쿠스틱 기타"),
    ELECTRIC_GUITAR("일렉 기타"),
    BASE("베이스"),
    SYNTHESIZER("신디사이저"),
    ETC("기타");
    private final String name;
}
