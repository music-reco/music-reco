package com.e106.reco.domain.artist.crew.entity;

import com.e106.reco.domain.artist.crew.dto.CreateDto;
import com.e106.reco.domain.artist.crew.dto.CrewDto;
import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.domain.artist.user.entity.User;
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
        return Crew.builder()
                .position(Position.CREW)
                .region(Region.of(createDto.getRegion()))
                .genre(Genre.of(createDto.getGenre()))

                .birth(createDto.getBirth())
                .nickname(createDto.getNickname())
                .content(createDto.getContent())
                .profileImage(createDto.getProfileImage())
                .build();
    }
    public static void of(Crew crew, CrewDto crewDto) {
        crew.modifyGenre(Genre.of(crewDto.getGenre()));
        crew.modifyRegion(Region.of(crewDto.getRegion()));
        crew.modifyBirth(crewDto.getBirth());
        crew.modifyNickname(crewDto.getNickname());
        crew.modifyContent(crewDto.getContent());
        crew.modifyProfileImage(crewDto.getProfileImage());
    }
}
