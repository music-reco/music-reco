package com.e106.reco.domain.workspace.entity;

import com.e106.reco.global.common.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyTree extends BaseTime {

    @EmbeddedId
    private PK pk;

    @MapsId("parentWorkspaceSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_workspace_seq")
    private Workspace parentWorkspace;

    @MapsId("childWorkspaceSeq")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_workspace_seq")
    private Workspace childWorkspace;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class PK implements Serializable {

        @Column(name = "parent_workspace_seq")
        private Long parentWorkspaceSeq;

        @Column(name = "child_workspace_seq")
        private Long childWorkspaceSeq;
    }
}
