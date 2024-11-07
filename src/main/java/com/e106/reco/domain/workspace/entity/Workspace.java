package com.e106.reco.domain.workspace.entity;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.workspace.entity.converter.WorkspaceState;
import com.e106.reco.global.common.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Workspaces")
public class Workspace extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workspace_seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_seq")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_workspace_seq")
    private Workspace parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_workspace_seq")
    private Workspace rootParent;

    private String name;

    private String originSinger;

    private String originTitle;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private WorkspaceState state;

    public void modifyState(WorkspaceState state) {
        this.state = state;
    }

    public void modifyThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static Workspace fork(Workspace workspace, Long artistSeq){
        return Workspace.builder()
                .name(workspace.getName() + "_복사본")
                .artist(Artist.builder()
                        .seq(artistSeq)
                        .build())
                .rootParent(workspace.getRootParent())
                .parent(workspace)
                .thumbnail(workspace.getThumbnail())
                .originTitle(workspace.originTitle)
                .originSinger(workspace.originSinger)
                .state(WorkspaceState.PRIVATE)
                .build();
    }
}
