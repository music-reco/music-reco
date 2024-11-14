package com.e106.reco.domain.artist.user.repository;

import com.e106.reco.domain.artist.user.node.InstrumentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends Neo4jRepository<InstrumentNode, Long> {
    Optional<InstrumentNode> findByName(String name);
}
