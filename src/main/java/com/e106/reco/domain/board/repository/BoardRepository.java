package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b where b.state != com.e106.reco.domain.board.entity.BoardState.INACTIVE")
    Optional<Board> findBySeq(Long seq);
}
