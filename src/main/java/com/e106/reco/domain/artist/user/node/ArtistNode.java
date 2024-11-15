package com.e106.reco.domain.artist.user.node;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Artist")
@Data
@NoArgsConstructor
public class ArtistNode {
    @Id
    @GeneratedValue
    private Long id;

    private Long artistSeq;

    private String name;

    @Relationship(type = "PLAYS_GENRE", direction = Relationship.Direction.OUTGOING)
    private GenreNode genres;

    @Relationship(type = "PLAYS_INSTRUMENT", direction = Relationship.Direction.OUTGOING)
    private InstrumentNode instruments;

    @Relationship(type = "BASED_IN", direction = Relationship.Direction.OUTGOING)
    private RegionNode region;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private Set<ArtistNode> followingArtists = new HashSet<>();
}