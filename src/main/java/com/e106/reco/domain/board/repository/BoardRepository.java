package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b where b.state != com.e106.reco.domain.board.entity.BoardState.INACTIVE " +
            "AND b.seq = :seq")
    Optional<Board> findBySeq(@Param("seq")Long seq);

    @Query("select b from Board b where b.state != com.e106.reco.domain.board.entity.BoardState.INACTIVE " +
            "AND b.artist.seq = :artist_seq")
    List<Board> findByArtist_seq(Long artist_seq, Pageable pageable);

    @Query("select b from Board b where b.state = com.e106.reco.domain.board.entity.BoardState.PUBLIC " +
            "AND b.artist.seq = :artist_seq")
    List<Board> findPublicBoardByArtist_seq(Long artist_seq, Pageable pageable);


}
