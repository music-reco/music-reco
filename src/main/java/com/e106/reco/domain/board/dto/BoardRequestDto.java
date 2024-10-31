package com.e106.reco.domain.board.dto;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.board.entity.Board;
import com.e106.reco.domain.board.entity.BoardState;
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
    private String title;
    private BoardState state;
    private String content;
//    private List<MultipartFile> files;

    public Board toEntity(Long artistSeq, String thumbnailFileName) {
        return Board.builder()
                .title(this.getTitle())
                .state(this.getState())
                .artist(Artist.builder().seq(artistSeq).build())
                .content(this.getContent())
                .thumbnail(thumbnailFileName)
                .build();
    }
}
