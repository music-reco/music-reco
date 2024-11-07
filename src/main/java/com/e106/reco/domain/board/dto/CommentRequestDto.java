package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.board.entity.Board;
import com.e106.reco.domain.board.entity.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentRequestDto {
    @NotNull
    private Long artistSeq;
    @NotNull
    private Long boardSeq;
    private Long parentCommentSeq;
    @NotNull
    private String content;

    public static Comment toEntity(CommentRequestDto comment) {
        return Comment.builder()
                .board(Board.builder().seq(comment.getBoardSeq()).build())
                .parent(Comment.builder().seq(comment.getParentCommentSeq()).build())
                .artist(Artist.builder().seq(comment.getArtistSeq()).build())
                .content(comment.getContent())
                .build();
    }
}
