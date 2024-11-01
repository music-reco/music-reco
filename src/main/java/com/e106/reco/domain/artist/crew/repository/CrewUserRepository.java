package com.e106.reco.domain.artist.crew.repository;

import com.e106.reco.domain.artist.crew.entity.CrewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewUserRepository extends JpaRepository<CrewUser, CrewUser.PK> {
    @Query("select count(*) from CrewUser cu where cu.pk.crewSeq = :crewSeq " +
            "AND cu.state != com.e106.reco.domain.artist.crew.entity.CrewUserState.WAITING"
    )
    int isPossibleCrewUserAccept(@Param("crewSeq") Long crewSeq);

    @Query("select count(cu) from CrewUser cu " +
            "where cu.pk.crewSeq = :crewSeq " +
            "and cu.pk.userSeq in :userSeqList " +
            "and cu.state != com.e106.reco.domain.artist.crew.entity.CrewUserState.WAITING")
    int countCrewUsers(@Param("crewSeq") Long crewSeq, @Param("userSeqList") List<Long> userSeqList);

    @Modifying
    @Query("delete from CrewUser cu where cu.pk.crewSeq = :crewSeq AND cu.pk.userSeq != :userSeq " +
            "AND cu.state != com.e106.reco.domain.artist.crew.entity.CrewUserState.WAITING")
    void deleteAllByCrewWithoutMaster(@Param("crewSeq") Long crewSeq, @Param("userSeq")Long userSeq);


    List<CrewUser> findCrewUsersByCrewSeq(Long crewSeq);
}