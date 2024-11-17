package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardsResponseDto {
    private ArtistSummaryDto artistSummary;
    private List<BoardResponseDto> boards;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoardResponseDto {

        private Long seq;
        private String title;
        private String state;
        private int comments;
        private LocalDateTime createTime;
        private int likes;
    }

    public static BoardResponseDto of(Board board, int comments, int likes) {
        return BoardResponseDto.builder()
                .seq(board.getSeq())
                .state(board.getState().name())
                .title(board.getTitle())
                .createTime(board.getCreatedAt())
                .likes(likes)
                .comments(comments)
                .build();
    }
    public static BoardsResponseDto of(List<BoardResponseDto> boards, ArtistSummaryDto artistSummary) {
        return BoardsResponseDto.builder()
                .artistSummary(artistSummary)
                .boards(boards)
                .build();
    }
}
