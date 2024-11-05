package com.e106.reco.domain.artist.user.dto;

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

    private String region;
    private String position;
    private String genre;

    private String profileImage;

    private String content;

    private List<Long> crews;

    public static UserInfoDto of(User user, List<Long> crews) {
        return UserInfoDto.builder()
                .seq(user.getSeq())
                .email(user.getEmail())
                .name(user.getName())
                .gender(user.getGender())
                .birth(user.getBirth())
                .nickname(user.getNickname())
                .genre(user.getGenre().name())
                .region(user.getRegion().name())
                .position(user.getPosition().name())
                .profileImage(user.getProfileImage())
                .content(user.getContent())
                .crews(crews).build();

    }
}