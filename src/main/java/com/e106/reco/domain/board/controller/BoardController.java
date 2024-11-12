package com.e106.reco.domain.board.controller;

import com.e106.reco.domain.board.dto.BoardRequestDto;
import com.e106.reco.domain.board.dto.BoardResponseDto;
import com.e106.reco.domain.board.dto.BoardsResponseDto;
import com.e106.reco.domain.board.dto.CommentRequestDto;
import com.e106.reco.domain.board.service.BoardService;
import com.e106.reco.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CommonResponse> createBoard(
            @RequestPart @Valid BoardRequestDto boardRequestDto,
            @RequestPart (value = "files", required = false) List<MultipartFile> files){
        return ResponseEntity.ok(boardService.createBoard(boardRequestDto, files));
    }

    @GetMapping("/{boardSeq}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardSeq,
                                                     @PageableDefault(direction = Sort.Direction.DESC, sort = "seq") Pageable pageable){
        return ResponseEntity.ok(boardService.getBoard(boardSeq, pageable));
    }

    @PostMapping(value = "/{boardSeq}", consumes = "multipart/form-data")
    public ResponseEntity<CommonResponse> updateBoard(
            @PathVariable Long boardSeq,
            @RequestPart @Valid BoardRequestDto boardRequestDto,
            @RequestPart (value = "files", required = false) List<MultipartFile> files){
        return ResponseEntity.ok(boardService.updateBoard(boardSeq, boardRequestDto, files));
    }

    @DeleteMapping("/{boardSeq}")
    public ResponseEntity<CommonResponse> deleteBoard(@PathVariable Long boardSeq){
        return ResponseEntity.ok(boardService.deleteBoard(boardSeq));
    }

    @GetMapping("/artists/{artistSeq}")
    public ResponseEntity<List<BoardsResponseDto>> getBoards(@PathVariable Long artistSeq,
                                                             @PageableDefault(direction = Sort.Direction.DESC, sort = "seq") Pageable pageable){
        return ResponseEntity.ok(boardService.getBoards(artistSeq, pageable));
    }

    @PostMapping("/comments")
    public ResponseEntity<CommonResponse> createComment(@RequestBody @Valid CommentRequestDto commentRequestDto){
        return ResponseEntity.ok(boardService.createComment(commentRequestDto));
    }

    @DeleteMapping("/comments/{commentSeq}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long commentSeq){
        return ResponseEntity.ok(boardService.deleteComment(commentSeq));
    }

    @PutMapping("/comments/{commentSeq}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long commentSeq, @RequestBody @Valid CommentRequestDto commentRequestDto){
        return ResponseEntity.ok(boardService.updateComment(commentRequestDto, commentSeq));
    }
}
