package com.e106.reco.domain.artist.user.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Genre")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GenreNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}