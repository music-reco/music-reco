package com.e106.reco.domain.artist.crew.repository;

import com.e106.reco.domain.artist.crew.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
    Optional<Crew> findBySeq(Long Seq);
}