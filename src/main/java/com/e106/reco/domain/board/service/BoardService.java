package com.e106.reco.domain.board.service;


import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.board.dto.BoardRequestDto;
import com.e106.reco.domain.board.entity.Board;
import com.e106.reco.domain.board.entity.Source;
import com.e106.reco.domain.board.repository.BoardRepository;
import com.e106.reco.domain.board.repository.SourceRepository;
import com.e106.reco.global.auth.jwt.JwtUtil;
import com.e106.reco.global.s3.S3FileService;
import com.e106.reco.global.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final SourceRepository sourceRepository;
    private final S3FileService s3FileService;

    public void createBoard(List<MultipartFile> files, BoardRequestDto boardRequestDto){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();
        Long userSeq = user.getSeq();

        // 아래 if문이 2개나 들어가는데.... 흠냐링... 뭔가 board만들때 썸네일까지 넣고싶었달까..
        String thumbnailFileName =  null;
        if(files != null){
            // 썸네일 뽑기
            Optional<MultipartFile> optionalThumbnailFile = files.stream().filter(file -> file.getContentType() != null && file.getContentType().startsWith("image")).findFirst();
            if(optionalThumbnailFile.isPresent()){
                thumbnailFileName = s3FileService.uploadThumbnail(optionalThumbnailFile.get());
            }
        }
        Board board = boardRequestDto.toEntity(userSeq, thumbnailFileName);
        boardRepository.save(board);

        if(files != null){


            List<String> fileNameList = s3FileService.uploadFiles(files);

            fileNameList.forEach(filename -> {
                Source source = Source.builder().board(board).name(filename).build();

                sourceRepository.save(source);
            });
        }

    }


}
