package com.e106.reco.domain.artist.crew.repository;

import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findBySeq(Long Seq);
    Optional<Crew> findBySeqAndManager(Long Seq, User user);
}