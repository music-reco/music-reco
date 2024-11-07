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

    @ManyToOne
    @JoinColumn(name = "parent_seq")
    private Comment parent;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder.Default
    private CommentState state = CommentState.ACTIVE;

    public void delete() {
        this.state = CommentState.INACTIVE;
    }
}
