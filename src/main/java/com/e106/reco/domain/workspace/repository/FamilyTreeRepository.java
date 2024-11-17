package com.e106.reco.domain.workspace.repository;


import com.e106.reco.domain.workspace.entity.FamilyTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FamilyTreeRepository extends JpaRepository<FamilyTree, Long> {
    List<FamilyTree> findAllByPk_ParentWorkspaceSeq(Long parentSeq);

    @Query("SELECT f FROM FamilyTree f " +
            "JOIN FETCH f.parentWorkspace p " +
            "JOIN FETCH f.childWorkspace c " +
            "JOIN FETCH p.artist a " +
            "WHERE f.childWorkspace.seq = :workspaceSeq " +
            "ORDER BY f.createdAt")
    List<FamilyTree> findAllByPk_ChildWorkspaceSeqOrderByCreatedAt(Long workspaceSeq);
}
