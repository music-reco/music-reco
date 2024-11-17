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
    private String seq;

    private Long artistSeq;
    private Long roomSeq;

    private String nickname;
    private String profilePicUrl;
    private LocalDateTime joinAt;

    public ChatArtist join(LocalDateTime joinAt){
        this.joinAt = joinAt;
        return this;
    }
    public void leave(){
        this.joinAt = null;
    }

    public static ChatArtist of(Artist artist) {
        return ChatArtist.builder()
                .artistSeq(artist.getSeq())
                .profilePicUrl(artist.getProfileImage())
                .nickname(artist.getNickname())
                .build();
    }

    public static ChatArtist of(Artist artist, Long roomSeq, LocalDateTime joinAt) {
        return ChatArtist.builder()
                .artistSeq(artist.getSeq())
                .roomSeq(roomSeq)
                .profilePicUrl(artist.getProfileImage())
                .nickname(artist.getNickname())
                .joinAt(joinAt)
                .build();
    }
}
