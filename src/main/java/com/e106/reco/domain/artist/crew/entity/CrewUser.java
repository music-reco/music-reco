package com.e106.reco.domain.artist.crew.entity;

import com.e106.reco.domain.artist.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "crew_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CrewUser{
    @EmbeddedId
    private PK pk;

    @MapsId("crewSeq")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "crew_seq")
    private Crew crew;

    @MapsId("userSeq")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CrewUserState state = CrewUserState.WAITING;

    @Builder
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
    public void modifyState(CrewUserState state){
        this.state = state;
    }

    public void acceptCrew(CrewUserState state){
        if(state == CrewUserState.WAITING) this.state = CrewUserState.NONE;
    }


    public static CrewUser of(User user, Crew crew) {
        return CrewUser.builder()
                .crew(crew)
                .user(user)
                .pk(PK.builder()
                        .crewSeq(crew.getSeq())
                        .userSeq(user.getSeq())
                    .build())
                .build();
    }
}
