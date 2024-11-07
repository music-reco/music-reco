package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.state != com.e106.reco.domain.board.entity.CommentState.INACTIVE")
    Optional<Comment> findBySeq(Long seq);

    List<Comment> findByBoard_Seq(Long seq);
    List<Comment> findByParent_seq(Long parentSeq);
}
