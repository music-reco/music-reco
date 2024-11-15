package com.e106.reco.domain.artist.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponse {
    private Long artistSeq;
    private String thumbnail;
    private String nickname;
}
