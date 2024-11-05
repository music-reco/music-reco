package com.e106.reco.domain.board.controller;

import com.e106.reco.domain.board.dto.BoardRequestDto;
import com.e106.reco.domain.board.entity.BoardState;
import com.e106.reco.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/boards", consumes = "multipart/form-data")
    public ResponseEntity<String> createBoard(
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "state") String state,
            @RequestParam(value = "content") String content){
        for (MultipartFile file : files) {
            System.out.println("file = " + file);
        }
        boardService.createBoard(files, BoardRequestDto.builder().content(content).state(BoardState.valueOf(state)).title(title).build());

        return ResponseEntity.ok("hi");
    }

}
