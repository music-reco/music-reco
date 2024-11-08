package com.e106.reco.domain.workspace.entity.converter;

import java.util.Arrays;

public enum StemType {
    VOCALS("vocals", SplitterType.values()),
    VOICE("voice", SplitterType.values()),
    DRUM("drum", new SplitterType[]{SplitterType.PHOENIX, SplitterType.ORION}),
    PIANO("piano", new SplitterType[]{SplitterType.PHOENIX, SplitterType.ORION}),
    BASS("bass", new SplitterType[]{SplitterType.PHOENIX, SplitterType.ORION}),
    ELECTRIC_GUITAR("electric_guitar", new SplitterType[]{SplitterType.PHOENIX, SplitterType.ORION}),
    ACOUSTIC_GUITAR("acoustic_guitar", new SplitterType[]{SplitterType.PHOENIX, SplitterType.ORION}),
    SYNTHESIZER("synthesizer", new SplitterType[]{SplitterType.PHOENIX}),
    STRINGS("strings", new SplitterType[]{SplitterType.PHOENIX}),
    WIND("wind", new SplitterType[]{SplitterType.PHOENIX});

    private final String value;
    private final SplitterType[] supportedSplitters;

    StemType(String value, SplitterType[] supportedSplitters) {
        this.value = value;
        this.supportedSplitters = supportedSplitters;
    }

    public String getValue() {
        return value;
    }

    public boolean isSupportedBy(SplitterType splitter) {
        return Arrays.asList(supportedSplitters).contains(splitter);
    }

    public static StemType fromString(String text) {
        return Arrays.stream(StemType.values())
                .filter(stemType -> stemType.value.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown stem type: " + text
                ));
    }
}