package com.e106.reco.domain.artist.user.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Instrument")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InstrumentNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}