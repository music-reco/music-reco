package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.board.entity.Board;
import com.e106.reco.domain.board.entity.BoardState;
import com.e106.reco.domain.board.entity.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardResponseDto {
    private Long boardSeq;
    private ArtistSummaryDto artistDto;
    private String title;
    private BoardState state;
    private String content;
    private List<String> sources;
    private List<CommentResponseDto> comments;
    private String liked;

    public static BoardResponseDto of(Board board, List<Source> sources, List<CommentResponseDto> comments, String liked) {
        return BoardResponseDto.builder()
                .boardSeq(board.getSeq())
                .artistDto(ArtistSummaryDto.of(board.getArtist()))
                .state(board.getState())
                .title(board.getTitle())
                .content(board.getContent())
                .sources(sources.stream().map(Source::getName).toList())
                .comments(comments)
                .liked(liked)
                .build();
    }
}
