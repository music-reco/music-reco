package com.e106.reco.domain.artist.user.node;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Region")
@Data
@Builder
public class RegionNode {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}