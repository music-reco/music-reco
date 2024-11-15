package com.e106.reco.domain.chat.entity;

import com.e106.reco.domain.artist.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_artist")
public class ChatArtist {

    @Id
    private String artistSeq;

    private String nickname;
    private String profilePicUrl;
    private String joinAt;
    private String roomSeq;

    public static ChatArtist of(Artist artist) {
        return ChatArtist.builder()
                .artistSeq(artist.getSeq().toString())
                .profilePicUrl(artist.getProfileImage())
                .nickname(artist.getNickname())
                .build();
    }

    public static ChatArtist of(Artist artist, LocalDateTime joinAt) {
        return ChatArtist.builder()
                .artistSeq(artist.getSeq().toString())
                .profilePicUrl(artist.getProfileImage())
                .nickname(artist.getNickname())
                .joinAt(joinAt.toString())
                .build();
    }
}
