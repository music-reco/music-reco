package com.e106.reco.domain.artist.crew.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CrewDto {
    @NotNull
    private Long crewSeq;

    private LocalDate birth;
    private String nickname;
    private String region;
    private String genre;
    private String content;

    private String profileImage;
}
