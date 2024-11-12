package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.board.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Long> {
    List<Source> findByBoard_seq(Long boardSeq);
    void deleteByBoard_seq(Long boardSeq);
}
