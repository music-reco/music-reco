package com.e106.reco.domain.board.controller;

import com.e106.reco.domain.board.dto.BoardRequestDto;
import com.e106.reco.domain.board.service.BoardService;
import com.e106.reco.global.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/boards", consumes = "multipart/form-data")
    public ResponseEntity<CommonResponse> createBoard(
            @RequestPart @Valid BoardRequestDto boardRequestDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files){

        boardService.createBoard(boardRequestDto, files);

        return ResponseEntity.ok(new CommonResponse("글 생성 완료"));
    }

}
