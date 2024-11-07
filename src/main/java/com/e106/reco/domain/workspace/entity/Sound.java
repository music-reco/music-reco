package com.e106.reco.domain.workspace.entity;

import com.e106.reco.domain.workspace.dto.midify.ModifyPoint;
import com.e106.reco.domain.workspace.entity.converter.SoundType;
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
@Table(name="sounds")
public class Sound extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sound_seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_seq")
    private Workspace workspace;

    private String url;

    private Double startPoint;

    private Double endPoint;

    @Enumerated(EnumType.STRING)
    private SoundType type;

    public void modifyPoint(ModifyPoint modifyPoint){
        this.startPoint = modifyPoint.getStartPoint();
        this.endPoint = modifyPoint.getEndPoint();
    }

    public static Sound fork(Sound sound, Long workspaceSeq){
        return Sound.builder()
                .workspace(Workspace.builder()
                        .seq(workspaceSeq)
                        .build())
                .startPoint(sound.getStartPoint())
                .endPoint(sound.getEndPoint())
                .url(sound.getUrl())
                .type(sound.getType())
                .build();
    }
}
