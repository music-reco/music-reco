package com.e106.reco.domain.artist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static com.e106.reco.domain.artist.entity.Position.크루;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "artists")
@Inheritance(strategy = InheritanceType.JOINED)
public class Artist {

    @Id
    @GeneratedValue
    @Column(name = "artist_seq")
    private Long seq;

    private LocalDate birth;
    private String nickname;

    @Builder.Default()
    private Position position = 크루;
    private Region region;
    private Genre genre;

    private String profileImage;
    private String content;

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }
    public void modifyBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void modifyRegion(Region region) { this.region = region; }
    public void modifyPosition(Position position) {
        this.position = position;
    }
    public void modifyGenre(Genre genre) {
        this.genre = genre;
    }

    public void modifyContent(String content) {
        this.content = content;
    }
    public void modifyProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
