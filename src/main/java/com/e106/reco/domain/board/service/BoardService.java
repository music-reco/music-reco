package com.e106.reco.domain.board.service;


import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.repository.ArtistRepository;
import com.e106.reco.domain.artist.user.dto.CustomUserDetails;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.board.dto.ArtistSummaryDto;
import com.e106.reco.domain.board.dto.BoardRequestDto;
import com.e106.reco.domain.board.dto.BoardResponseDto;
import com.e106.reco.domain.board.dto.BoardsResponseDto;
import com.e106.reco.domain.board.dto.CommentRequestDto;
import com.e106.reco.domain.board.dto.CommentResponseDto;
import com.e106.reco.domain.board.entity.Board;
import com.e106.reco.domain.board.entity.BoardState;
import com.e106.reco.domain.board.entity.Comment;
import com.e106.reco.domain.board.entity.Like;
import com.e106.reco.domain.board.entity.Source;
import com.e106.reco.domain.board.repository.BoardRepository;
import com.e106.reco.domain.board.repository.CommentRepository;
import com.e106.reco.domain.board.repository.LikeRepository;
import com.e106.reco.domain.board.repository.SourceRepository;
import com.e106.reco.global.common.CommonResponse;
import com.e106.reco.global.error.errorcode.BoardErrorCode;
import com.e106.reco.global.error.errorcode.CommonErrorCode;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.s3.S3FileService;
import com.e106.reco.global.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.e106.reco.global.error.errorcode.ArtistErrorCode.ARTIST_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.BoardErrorCode.BOARD_GRANT_FAIL;
import static com.e106.reco.global.error.errorcode.BoardErrorCode.BOARD_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.BoardErrorCode.COMMENT_DEEP_ONLY_ONE;
import static com.e106.reco.global.error.errorcode.BoardErrorCode.COMMENT_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private final ArtistRepository artistRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CrewUserRepository crewUserRepository;
    private final SourceRepository sourceRepository;
    private final S3FileService s3FileService;
    private final LikeRepository likeRepository;
    private final int FILE_SIZE = 15;

    public BoardsResponseDto getBoards(Long artistSeq, Pageable pageable) {
        CustomUserDetails user = AuthUtil.getCustomUserDetails();

        Artist artist = artistRepository.findBySeq(artistSeq)
                .orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));

        List<Board> boards;
        if(artist.getPosition() == Position.CREW && user.getCrews().contains(artistSeq) ||
                artist.getPosition() != Position.CREW && user.getSeq().equals(artistSeq))
            boards = boardRepository.findByArtist_seq(artistSeq, pageable);
        else
            boards = boardRepository.findPublicBoardByArtist_seq(artistSeq, pageable);

        return BoardsResponseDto.of(
                boards.stream().map(board -> BoardsResponseDto.of(board,
                        commentRepository.countByBoard_Seq(board.getSeq()),
                        likeRepository.countByPk_BoardSeq(board.getSeq()))).toList(),
                ArtistSummaryDto.of(artist)
        );
    }

    public BoardResponseDto getBoard(Long boardSeq, Pageable pageable){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();

        Board board = boardRepository.findBySeq(boardSeq)
                .orElseThrow(()->new BusinessException(BOARD_NOT_FOUND));
        Artist artist = board.getArtist();

        if(board.getState() == BoardState.PRIVATE){

            if(artist.getPosition() == Position.CREW && !user.getCrews().contains(artist.getSeq()))
                return null;
            else if(artist.getPosition() != Position.CREW && !user.getSeq().equals(artist.getSeq()))
                return null;
        }

        List<Source> sources = sourceRepository.findByBoard_seq(boardSeq);
        List<CommentResponseDto> comments = getComment(boardSeq, pageable);
        if(isLike(user.getSeq(), boardSeq))
            return BoardResponseDto.of(board, sources, comments, "liked");

        return BoardResponseDto.of(board, sources, comments, "none");
    }
    public CommonResponse updateComment(CommentRequestDto commentRequestDto, Long commentSeq){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();
        Comment comment = commentRepository.findBySeq(commentSeq)
                        .orElseThrow(()->new BusinessException(COMMENT_NOT_FOUND));

        artistCertification(user.getSeq(), comment.getArtist());

        comment.changeContent(commentRequestDto.getContent());
        comment.changeUpdatedAt();

        return new CommonResponse("댓글 수정 완료");
    }
    public CommonResponse createComment(CommentRequestDto commentRequestDto){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();

        Artist artist = artistRepository.findBySeq(commentRequestDto.getArtistSeq())
                .orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));
        Board board = boardRepository.findBySeq(commentRequestDto.getBoardSeq())
                .orElseThrow(()-> new BusinessException(BOARD_NOT_FOUND));
        Comment parentComment = commentRequestDto.getParentCommentSeq()!=null ?
                commentRepository.findBySeq(commentRequestDto.getParentCommentSeq())
                        .orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND)) : null;

        if(parentComment != null && parentComment.getParent()!=null)
            throw new BusinessException(COMMENT_DEEP_ONLY_ONE);


        // 아티스트 시퀸스가 crew일때 권한 검증
        artistCertification(user.getSeq(), artist);

        commentRepository.save(Comment.builder()
                .parent(parentComment)
                .board(board)
                .artist(artist)
                .content(commentRequestDto.getContent())
                .build()
        );
        return new CommonResponse("댓글 생성 완료");
    }


    public CommonResponse deleteComment(Long commentSeq){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();

        Comment comment = commentRepository.findBySeq(commentSeq)
                .orElseThrow(()-> new BusinessException(COMMENT_NOT_FOUND));

        // 아티스트 시퀸스가 crew일때 권한 검증
        artistCertification(user.getSeq(), comment.getArtist());

        commentRepository.findByParent_seq(commentSeq).forEach(Comment::delete);
        comment.delete();
        return new CommonResponse("댓글 삭제 완료");
    }
    public List<CommentResponseDto> getComment(Long boardSeq, Pageable pageable){
        List<Comment> comments = commentRepository.findByBoard_Seq(boardSeq, pageable);
        return comments.stream().map(CommentResponseDto::of).toList();
    }

    public CommonResponse createBoard(BoardRequestDto boardRequestDto, List<MultipartFile> files){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();

        if(files!=null && files.size()> FILE_SIZE) throw new BusinessException(BoardErrorCode.BOARD_SOURCE_ONLY_15);

        Artist artist = artistRepository.findBySeq(boardRequestDto.getArtistSeq())
                .orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));

        // 아티스트 시퀸스가 crew일때 권한 검증
        artistCertification(user.getSeq(), artist);


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
        Board board = Board.builder()
                .artist(artist)
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .state(BoardState.of(boardRequestDto.getState()))
                .thumbnail(null)
                .build();

        log.info("야호야호");
        log.info("files : {}", files);
        Board savedBoard = boardRepository.save(board);
        log.info("savedBoard : {}", savedBoard.getSeq());
        if(files != null){
            log.info("파일 넣기 시작");
            List<String> fileNameList = s3FileService.uploadFiles(files);

            fileNameList.forEach(filename -> {
                log.info("fileName: {}", filename);
                Source source = Source.builder()
                        .board(board)
                        .name(filename)
                        .build();
                log.info("sourceSeq : {}", source.getSeq());
                sourceRepository.save(source);
            });
        }

        return new CommonResponse("글 작성 완료");
    }

    private void artistCertification(Long userSeq, Artist artist) {
        if( artist.getPosition() == Position.CREW ) {
            CrewUser crewUser = crewUserRepository.findCrewUserByPk(CrewUser.PK.builder()
                            .crewSeq(artist.getSeq())
                            .userSeq(userSeq)
                            .build())
                    .orElseThrow(()-> new BusinessException(CREW_USER_NOT_FOUND));

            if(crewUser.getState() != CrewUserState.ALL && crewUser.getState() != CrewUserState.BOARD)
                throw new BusinessException(BOARD_GRANT_FAIL);
        } else if(!artist.getSeq().equals(userSeq)) throw new BusinessException(BOARD_GRANT_FAIL);
    }
    public CommonResponse updateBoard(Long boardSeq, BoardRequestDto boardRequestDto, List<MultipartFile> files){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();
        Board board = boardRepository.findBySeq(boardSeq).orElseThrow(()-> new BusinessException(BOARD_NOT_FOUND));
        artistCertification(user.getSeq(), board.getArtist());

        sourceRepository.deleteByBoard_seq(boardSeq);
        if(files != null){
            List<String> fileNameList = s3FileService.uploadFiles(files);

            fileNameList.forEach(filename -> {
                Source source = Source.builder().board(board).name(filename).build();

                sourceRepository.save(source);
            });
        }
        board.changeTitle(boardRequestDto.getTitle());
        board.changeContent(boardRequestDto.getContent());
        board.changeState(BoardState.of(boardRequestDto.getState()));
        board.changeUpdatedAt();
        return new CommonResponse("글 수정 완료");
    }
    public CommonResponse deleteBoard(Long boardSeq){
        CustomUserDetails user = AuthUtil.getCustomUserDetails();
        Board board = boardRepository.findBySeq(boardSeq).orElseThrow(()-> new BusinessException(BOARD_NOT_FOUND));
        artistCertification(user.getSeq(), board.getArtist());

        board.changeState(BoardState.INACTIVE);
        board.changeUpdatedAt();
        return new CommonResponse("글 수정 완료");
    }

    private boolean isLike(Long userSeq, Long boardSeq){
        return likeRepository.hasLike(userSeq, boardSeq);
    }

    public CommonResponse like(Long boardSeq) {
        Long userSeq = AuthUtil.getCustomUserDetails().getSeq();

        if(isLike(userSeq, boardSeq))
            throw new BusinessException(CommonErrorCode.valueOf("이미 좋아요한 글입니다."));

        Like like = Like.builder()
                .pk(new Like.PK(boardSeq, userSeq))
                .board(Board.builder()
                        .seq(boardSeq)
                        .build())
                .user(User.builder()
                        .seq(userSeq)
                        .build())
                .build();

        likeRepository.save(like);

        return new CommonResponse("좋아요 완료!");
    }

    public CommonResponse cancelLike(Long boardSeq) {
        Long userSeq = AuthUtil.getCustomUserDetails().getSeq();

        Like like = likeRepository.findById(new Like.PK(boardSeq, userSeq))
                .orElseThrow(() -> new BusinessException(CommonErrorCode.valueOf("좋아요가 되어있지 않습니다.")));

        likeRepository.delete(like);

        return new CommonResponse("좋아요 삭제");
    }
}
