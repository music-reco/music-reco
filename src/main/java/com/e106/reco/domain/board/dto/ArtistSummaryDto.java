package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.artist.entity.Artist;
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
    private String nickname;

    public static ArtistSummaryDto of(Artist artist) {
        return ArtistSummaryDto.builder()
                .seq(artist.getSeq())
                .nickname(artist.getNickname())
                .profileImage(artist.getProfileImage())
                .build();
    }
}