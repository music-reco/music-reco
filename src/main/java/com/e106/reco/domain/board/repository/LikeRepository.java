package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.board.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Like.PK> {

    @Query("SELECT EXISTS (" +
            "SELECT 1 FROM Like l " +
            "WHERE l.pk.boardSeq = :boardSeq " +
            "AND l.pk.userSeq = :userSeq " +
            ") ")
    boolean hasLike(Long userSeq, Long boardSeq);

    Integer countByPk_BoardSeq(Long boardSeq);
}
