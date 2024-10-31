package com.e106.reco.domain.artist.crew.entity;

import com.e106.reco.domain.artist.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "crews_users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="CREWS_USERS_UNIQUE",
                        columnNames={"seq","seq"}
                )})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CrewsUsers{
    @EmbeddedId
    @Column(name = "crew_user_seq")
    private PK pk;

    @MapsId("crewSeq")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "crew_seq")
    private Crew crew;

    @MapsId("userSeq")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @Builder.Default
    private CrewUserState status = CrewUserState.NONE;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class PK implements Serializable {
        @Column(name = "crew_seq")
        private Long crewSeq;

        @Column(name = "user_seq")
        private Long userSeq;
    }

    public void grantChat(CrewUserState state){
        if(state.equals(CrewUserState.ALL)) this.status = CrewUserState.ALL;
        else if(state.equals(CrewUserState.BOARD)) this.status = CrewUserState.ALL;
        else this.status = CrewUserState.CHAT;
    }
    public void revokeChat(CrewUserState state){
        if(state.equals(CrewUserState.ALL)) this.status = CrewUserState.BOARD;
        else this.status = CrewUserState.NONE;
    }

}
