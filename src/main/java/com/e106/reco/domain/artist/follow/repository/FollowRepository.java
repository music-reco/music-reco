package com.e106.reco.domain.artist.follow.repository;

import com.e106.reco.domain.artist.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {

    @Query("SELECT f FROM Follow f WHERE f.pk.targetSeq = :targetSeq AND f.pk.fanSeq = :fanSeq")
    boolean existsByFollow(Long targetSeq, Long fanSeq);

//    boolean existsByPk_TargetSeqAndPk_FanSeq(Long targetSeq, Long fanSeq);

    Optional<Follow> findByPk_TargetSeqAndPk_FanSeq(Long targetSeq, Long FanSeq);

    @Query("SELECT f FROM Follow f " +
            "JOIN FETCH f.targetUser target " +
            "where f.pk.fanSeq = :fanSeq")
    List<Follow> findByPk_fanSeq(Long fanSeq);

    @Query("SELECT f FROM Follow f " +
            "JOIN FETCH f.fanUser fan " +
            "where f.pk.targetSeq = :targetSeq")
    List<Follow> findByPk_targetSeq(Long targetSeq);
}