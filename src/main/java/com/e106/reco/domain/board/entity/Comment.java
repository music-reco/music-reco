package com.e106.reco.domain.board.entity;

import com.e106.reco.domain.artist.entity.Artist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "artist_seq", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "board_seq", nullable = false)
    private Board board;


    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}
