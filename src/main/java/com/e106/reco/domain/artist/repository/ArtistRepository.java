package com.e106.reco.domain.artist.repository;


import com.e106.reco.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findBySeq(Long seq);

    @Query("select a from Artist a where a.nickname like %:nickname%")
    List<Artist> findByNickname(@Param("nickname") String nickname);

}
