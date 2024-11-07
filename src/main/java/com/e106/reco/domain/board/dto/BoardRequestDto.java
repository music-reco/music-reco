package com.e106.reco.domain.board.dto;

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
public class BoardRequestDto {
    private Long artistSeq;
    private String title;
    private String state;
    private String content;
}
