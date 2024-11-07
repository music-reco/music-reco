package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommentResponseDto {
    private ArtistSummaryDto artistSummaryDto;
    private Long boardSeq;
    private Long parentCommentSeq;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .artistSummaryDto(ArtistSummaryDto.of(comment.getArtist()))
                .boardSeq(comment.getBoard().getSeq())
                .parentCommentSeq(comment.getParent() == null ? null : comment.getParent().getSeq())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }
}
