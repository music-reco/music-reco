package com.e106.reco.domain.artist.crew.repository;

import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.crew.entity.CrewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CrewUserRepository extends JpaRepository<CrewUser, CrewUser.PK> {
    @Query("select count(*) from CrewUser cu where cu.pk.crewSeq = :crewSeq " +
            "AND cu.state != com.e106.reco.domain.artist.crew.entity.CrewUserState.WAITING"
    )
    Integer isPossibleCrewUserAccept(Crew crew);
}