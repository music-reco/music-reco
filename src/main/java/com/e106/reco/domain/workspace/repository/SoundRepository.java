package com.e106.reco.domain.workspace.repository;

import com.e106.reco.domain.workspace.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoundRepository extends JpaRepository<Sound, Long> {
    List<Sound> findAllByWorkspace_Seq(Long workspaceSeq);
}
