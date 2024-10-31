package com.e106.reco.domain.artist.crew.repository;

import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {
}