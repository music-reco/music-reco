package com.e106.reco.domain.workspace.entity.converter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SoundType {
    PIANO("피아노","piano"),
    VOCAL("보컬","vocals"),
    ACOUSTIC_GUITAR("어쿠스틱 기타", "acoustic_guitar"),
    ELECTRIC_GUITAR("일렉 기타", "electric_guitar"),
    DRUM("드럼", "drum"),
    BASS("베이스","bass"),
    SYNTHESIZER("신디사이저","synthesizer"),
    ETC("기타","etc");
    private final String name;
    private final String englishName;

    public static SoundType fromString(String source) {
        for (SoundType soundType : SoundType.values()) {
            if (soundType.getName().equals(source)) {
                return soundType;
            }
            if(soundType.getEnglishName().equals(source)) {
                return soundType;
            }
        }
        throw new IllegalArgumentException(source);
    }
}
