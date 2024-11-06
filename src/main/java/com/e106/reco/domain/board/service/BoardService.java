package com.e106.reco.domain.board.service;


import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.board.dto.BoardRequestDto;
import com.e106.reco.domain.board.repository.ArtistRepository;
import com.e106.reco.domain.board.repository.BoardRepository;
import com.e106.reco.domain.board.repository.SourceRepository;
import com.e106.reco.global.error.errorcode.BoardErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.s3.S3FileService;
import com.e106.reco.global.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.e106.reco.global.error.errorcode.ArtistErrorCode.ARTIST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final ArtistRepository artistRepository;
    private final BoardRepository boardRepository;
    private final CrewUserRepository crewUserRepository;
    private final SourceRepository sourceRepository;
    private final S3FileService s3FileService;
    private final int FILE_SIZE = 15;

    public void createBoard(BoardRequestDto boardRequestDto, List<MultipartFile> files){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();

        Artist artist = artistRepository.findBySeq(boardRequestDto.getArtistSeq())
                .orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));

//        if( artist.getPosition() == Position.CREW ) {
//            CrewUser crewUser = crewUserRepository.find(boardRequestDto.getArtistSeq(), user.getSeq())
//        }

        if(files.size()> FILE_SIZE) throw new BusinessException(BoardErrorCode.BOARD_SOURCE_ONLY_15);

        // 아티스트 시퀸스가 crew일때 권한 검증

        // TODO :  썸네일은 다음에 하는 거 생각해볼게 미안해 현규움 ㅋ
        // 아래 if문이 2개나 들어가는데.... 흠냐링... 뭔가 board만들때 썸네일까지 넣고싶었달까..
        String thumbnailFileName =  null;
//        if(files != null){
//            // 썸네일 뽑기
//            Optional<MultipartFile> optionalThumbnailFile = files.stream().filter(file -> file.getContentType() != null && file.getContentType().startsWith("image")).findFirst();
//
//            if(optionalThumbnailFile.isPresent()){
//                thumbnailFileName = s3FileService.uploadThumbnail(optionalThumbnailFile.get());
//            }
//        }
//        boardRepository.save(board);

//        if(files != null){
//
//
//            List<String> fileNameList = s3FileService.uploadFiles(files);
//
//            fileNameList.forEach(filename -> {
//                Source source = Source.builder().board(board).name(filename).build();
//
//                sourceRepository.save(source);
//            });
//        }

    }


}
