package com.e106.reco.domain.workspace.entity.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SoundTypeConverter implements Converter<String, SoundType> {

    @Override
    public SoundType convert(String source) {
        for (SoundType soundType : SoundType.values()) {
            if (soundType.getName().equals(source)) {
                return soundType;
            }
        }
        throw new IllegalArgumentException("Invalid sound type: " + source);
    }
}
