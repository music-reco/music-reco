package com.e106.reco.domain.artist.crew.repository;

import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CrewUserRepository extends JpaRepository<CrewUser, CrewUser.PK> {
    @Query("select count(*) from CrewUser cu where cu.pk.crewSeq = :crewSeq " +
            "AND cu.state != com.e106.reco.domain.artist.crew.entity.CrewUserState.WAITING"
    )
    int isPossibleCrewUserAccept(@Param("crewSeq") Long crewSeq);

    @Query("select c from CrewUser c where c.pk.crewSeq = :crewSeq AND c.pk.userSeq != :userSeq " +
            "AND c.state != com.e106.reco.domain.artist.crew.entity.CrewUserState.WAITING")
    List<CrewUser> findCrewUsersByCrewSeqWithoutMaster(@Param("crewSeq")Long crewSeq, @Param("userSeq")Long userSeq);

    @Query("select pk.crewSeq from CrewUser where pk.userSeq = :userSeq ")
    List<Long> findPk_CrewSeqByPk_userSeq(@Param("userSeq")Long userSeq);

    Optional<CrewUser> findByPkAndState(CrewUser.PK pk, CrewUserState state);

    Optional<CrewUser> findCrewUserByPk(CrewUser.PK pk);
}