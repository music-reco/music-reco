package com.e106.reco.domain.board.entity;

import com.e106.reco.domain.artist.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "likes")
public class Like {

    @EmbeddedId
    @Column(name = "like_seq")
    private PK pk;

    @MapsId(value = "boardSeq")
    @ManyToOne
    @JoinColumn(name = "board_seq", nullable = false)
    private Board board;

    @MapsId(value = "userSeq")
    @ManyToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class PK implements Serializable {
        @Column(name = "board_seq")
        private Long boardSeq;

        @Column(name = "user_seq")
        private Long userSeq;
    }
}
