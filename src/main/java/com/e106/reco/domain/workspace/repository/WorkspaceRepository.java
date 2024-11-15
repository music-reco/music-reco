package com.e106.reco.domain.workspace.repository;

import com.e106.reco.domain.workspace.entity.Workspace;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT w FROM Workspace w WHERE w.artist.seq = :artistSeq AND w.state != :state")
    Page<Workspace> findByArtistSeqAndStateNot(Long artistSeq, WorkspaceState state, Pageable pageable);

    @Query("SELECT w FROM Workspace w JOIN FETCH w.artist a " +
            "WHERE w.seq = :artistSeq")
    Optional<Workspace> findByArtistSeq(Long artistSeq);
}
