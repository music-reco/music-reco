package com.e106.reco.domain.artist.dto;

import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.user.dto.UserSummaryDto;
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
public class ArtistSummaryDto {
    private Long seq;
    private String profileImage;
    private String email;
    private String nickname;
    private Position position;
    private UserSummaryDto manage;
}