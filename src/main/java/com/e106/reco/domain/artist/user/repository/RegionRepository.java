package com.e106.reco.domain.artist.user.repository;

import com.e106.reco.domain.artist.user.node.RegionNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends Neo4jRepository<RegionNode, Long> {
    Optional<RegionNode> findByName(String name);
}
