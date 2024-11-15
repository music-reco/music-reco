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
    Long ArtistSeq;

    public static ChatRoomResponse of(Artist artist){
        return ChatRoomResponse.builder().nickname(artist.getNickname()).ArtistSeq(artist.getSeq()).build();
    }
}
