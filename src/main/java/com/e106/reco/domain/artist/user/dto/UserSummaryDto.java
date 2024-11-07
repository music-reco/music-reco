package com.e106.reco.domain.artist.user.dto;

import com.e106.reco.domain.artist.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserSummaryDto {
    private Long seq;
    private String profileImage;
    private String email;
    private String nickname;

    public static UserSummaryDto of(User user) {
        return UserSummaryDto.builder()
                .seq(user.getSeq())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }
}