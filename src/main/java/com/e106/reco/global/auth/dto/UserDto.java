package com.e106.reco.global.auth.dto;

import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long seq;
    private String email;
    private String password;
    private String name;

    private Gender gender;

    private LocalDate birth;
    private String nickname;

    private Region region;
    private Position position;
    private Genre genre;

    private String profileImage;
}