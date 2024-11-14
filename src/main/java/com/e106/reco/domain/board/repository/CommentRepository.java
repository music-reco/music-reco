package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.board.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.state != com.e106.reco.domain.board.entity.CommentState.INACTIVE " +
            "AND c.seq = :seq")
    Optional<Comment> findBySeq(Long seq);

    @Query("select c from Comment c where c.board.seq = :boardSeq And c.state != com.e106.reco.domain.board.entity.CommentState.INACTIVE")
    List<Comment> findByBoard_Seq(Long boardSeq, Pageable pageable);

    @Query("select c from Comment c where c.parent.seq = :parentSeq And c.state != com.e106.reco.domain.board.entity.CommentState.INACTIVE")
    List<Comment> findByParent_seq(Long parentSeq);

    int countByBoard_Seq(Long boardSeq);
}
