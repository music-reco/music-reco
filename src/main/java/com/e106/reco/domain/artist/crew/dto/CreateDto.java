package com.e106.reco.domain.artist.crew.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;


@Getter
public class CreateDto {

    @NotNull
    private LocalDate birth;

    @NotBlank(message = "크루명은 공백이 될 수 없습니다.")
    private String nickname;

    @NotBlank(message = "지역은 공백이 될 수 없습니다.")
    private String region;

    @NotBlank(message = "장르는 공백이 될 수 없습니다.")
    private String genre;

    @NotBlank(message = "소개는 공백이 될 수 없습니다.")
    private String content;

    private String profileImage;
}
