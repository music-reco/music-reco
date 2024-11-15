package com.e106.reco.domain.chat.dto;

import com.e106.reco.domain.artist.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    String nickname;
    Long artistSeq;
    String artistImage;

    public static ChatRoomResponse of(Artist artist){
        return ChatRoomResponse.builder().nickname(artist.getNickname()).artistSeq(artist.getSeq()).artistImage(artist.getProfileImage()).build();
    }
}
