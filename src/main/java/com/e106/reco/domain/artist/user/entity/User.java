package com.e106.reco.domain.artist.user.entity;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.entity.Genre;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.entity.Region;
import com.e106.reco.global.auth.dto.JoinDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User extends Artist {
    @Column(unique=true)
    @Email
    private String email;

    private String password;
    private String name;

    private Gender gender;

    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    public void modifyPassword(String password) {
        this.password = password;
    }

    public static User of(JoinDto joinDto) {
        User newUser = User.builder()
                .email(joinDto.getEmail())
                .name(joinDto.getName())
                .gender(Gender.of(joinDto.getGender()))
                .status(UserStatus.ACTIVE)
                .build();

        newUser.modifyBirth(joinDto.getBirth());

        newUser.modifyRegion(Region.of(joinDto.getRegion()));
        newUser.modifyGenre(Genre.of(joinDto.getGenre()));
        newUser.modifyPosition(Position.of(joinDto.getPosition()));

        newUser.modifyNickname(joinDto.getNickname());
        newUser.modifyProfileImage(joinDto.getProfileImage());

        return newUser;
    }

    public static void of(User user, JoinDto joinDto) {
        user.modifyBirth(joinDto.getBirth());
        user.modifyNickname(joinDto.getNickname());
        user.modifyProfileImage(joinDto.getProfileImage());
        user.modifyContent(joinDto.getContent());
        user.modifyRegion(Region.of(joinDto.getRegion()));
        user.modifyGenre(Genre.of(joinDto.getGenre()));
        user.modifyPosition(Position.of(joinDto.getPosition()));
    }
}