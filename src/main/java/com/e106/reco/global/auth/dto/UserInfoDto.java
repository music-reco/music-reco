package com.e106.reco.global.auth.dto;

import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.entity.Gender;
import com.e106.reco.domain.artist.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoDto {
    private Long seq;
    private String email;
    private String name;

    private Gender gender;

    private LocalDate birth;
    private String nickname;

    private Region region;
    private Position position;
    private Genre genre;

    private String profileImage;

    private List<Long> crews;

    public static UserInfoDto of(User user, List<Long> crews) {
        return UserInfoDto.builder()
                .seq(user.getSeq())
                .email(user.getEmail())
                .name(user.getName())
                .gender(user.getGender())
                .birth(user.getBirth())
                .nickname(user.getNickname())
                .genre(user.getGenre())
                .region(user.getRegion())
                .position(user.getPosition())
                .profileImage(user.getProfileImage())
                .crews(crews).build();
    }
}