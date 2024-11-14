package com.e106.reco.domain.artist.user.repository;

import com.e106.reco.domain.artist.user.node.GenreNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends Neo4jRepository<GenreNode, Long> {
    Optional<GenreNode> findByName(String name);
}
