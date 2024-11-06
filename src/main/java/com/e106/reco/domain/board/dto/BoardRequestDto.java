package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.board.entity.BoardState;
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
public class BoardRequestDto {
    private Long artistSeq;
    private String title;
    private BoardState state;
    private String content;
    private List<String> files;

//    public Board toEntity(Long artistSeq, String thumbnailFileName) {
//        return Board.builder()
//
//                .title(this.getTitle())
//                .content(this.getContent())
//                .state(this.getState())
//                .artist(Artist.builder().seq(artistSeq).build())
//                .thumbnail(thumbnailFileName)
//                .build();
//    }
}
