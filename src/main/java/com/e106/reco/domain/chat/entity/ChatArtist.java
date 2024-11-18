package com.e106.reco.domain.chat.entity;

import com.e106.reco.domain.artist.entity.Artist;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.ALWAYS)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_artist")
public class ChatArtist {

    @Id
    private PK pk;

    private String nickname;
    private String profilePicUrl;

    @Column(nullable = true)
    @Field("joinAt")
    private String joinAt;

    @SuperBuilder
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class PK implements Serializable {
        private Long artistSeq;
        private Long roomSeq;
    }

    public ChatArtist join(LocalDateTime joinAt){
        this.joinAt = joinAt.toString();
        return this;
    }
    public void leave(){
        this.joinAt = null;
    }

    public static ChatArtist of(Artist artist) {
        return ChatArtist.builder()
                .pk(PK.builder().artistSeq(artist.getSeq()).build())
                .profilePicUrl(artist.getProfileImage())
                .nickname(artist.getNickname())
                .build();
    }

    public static ChatArtist of(Artist artist, Long roomSeq, LocalDateTime joinAt) {
        return ChatArtist.builder()
                .pk(PK.builder().artistSeq(artist.getSeq()).roomSeq(roomSeq).build())
                .profilePicUrl(artist.getProfileImage())
                .nickname(artist.getNickname())
                .joinAt(joinAt.toString())
                .build();
    }
}
