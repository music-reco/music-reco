package com.e106.reco.domain.chat.entity;

import com.e106.reco.domain.artist.entity.Artist;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @EmbeddedId
    private PK pk;

    @MapsId(value = "artistSeq")
    @ManyToOne
    @JoinColumn(name = "artist_seq", nullable = false)
    private Artist artist;

    @MapsId(value = "roomSeq")
    @ManyToOne
    @JoinColumn(name = "room_seq", nullable = false)
    private Room room;

    private RoomState state;

    @CreatedDate
    private LocalDateTime joinAt;

    private LocalDateTime lastActiveAt;

    @SuperBuilder
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class PK implements Serializable {
        @Column(name = "artist_seq")
        private Long artistSeq;

        @Column(name = "room_seq")
        private Long roomSeq;
    }

    public void leaveChatRoom(RoomState state) {
        this.state = RoomState.INACTIVE;
        this.joinAt = null;
    }
    public void joinChatRoom() {
        this.state = RoomState.ACTIVE;
        this.joinAt = LocalDateTime.now();
    }
    public void updateLastActiveAt() {
        this.lastActiveAt = LocalDateTime.now();
    }
}