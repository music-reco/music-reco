package com.e106.reco.domain.board.entity;

import com.e106.reco.domain.artist.entity.Artist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "boards")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "artist_seq", nullable = false)
    private Artist artist;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    private String title;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardState state = BoardState.PUBLIC;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private String thumbnail;

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


}
