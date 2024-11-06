package com.e106.reco.domain.board.repository;

import com.e106.reco.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findBySeq(Long seq);
}
