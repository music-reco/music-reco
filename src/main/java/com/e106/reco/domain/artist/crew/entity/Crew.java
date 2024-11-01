package com.e106.reco.domain.artist.crew.entity;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.entity.User;
import com.e106.reco.domain.artist.user.entity.UserStatus;
import com.e106.reco.global.auth.dto.JoinDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Getter
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "crews")
public class Crew extends Artist {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manage_seq")
    private User manager;

    public void modifyManagerSeq(User manager) {
        this.manager = manager;
    }

    public static Crew of(CreateDto createDto) {
        Crew newCrew = Crew.builder()
                .build();

        newCrew.modifyRegion(Region.of(createDto.getRegion()));
        newCrew.modifyGenre(Genre.of(createDto.getGenre()));

        newCrew.modifyBirth(createDto.getBirth());
        newCrew.modifyNickname(createDto.getNickname());
        newCrew.modifyContent(createDto.getContent());

//        newCrew.modifyProfileImage(createDto.getProfileImage());

        return newCrew;
    }
}
