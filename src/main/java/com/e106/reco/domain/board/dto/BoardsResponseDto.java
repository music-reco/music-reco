package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.board.entity.Board;
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
public class BoardsResponseDto {
    private Long seq;
    private String title;
    private String state;
    private int comments;
//    private int likes;

    public static BoardsResponseDto of(Board board, int comments) {
        return BoardsResponseDto.builder()
                .seq(board.getSeq())
                .state(board.getState().name())
                .title(board.getTitle())
                .comments(comments)
                .build();
    }
}
