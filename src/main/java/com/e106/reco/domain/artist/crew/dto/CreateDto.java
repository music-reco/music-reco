package com.e106.reco.domain.artist.crew.dto;

import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

import static com.e106.reco.global.util.RegExpUtils.EMAIL_EXP;
import static com.e106.reco.global.util.RegExpUtils.NAME_EXP;

public class CreateDto {

    @NotBlank(message = "창단일은 공백이 될 수 없습니다")
    private LocalDate birth;

    @NotBlank(message = "크루명은 공백이 될 수 없습니다.")
    private String nickname;

    @NotBlank(message = "지역은 공백이 될 수 없습니다.")
    private Region region;

    @NotBlank(message = "장르는 공백이 될 수 없습니다.")
    private Genre genre;

    @NotBlank(message = "소개는 공백이 될 수 없습니다.")
    private String content;

    private String profileImage;
}
