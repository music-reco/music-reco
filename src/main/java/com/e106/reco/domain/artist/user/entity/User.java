package com.e106.reco.domain.artist.user.entity;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.global.auth.dto.JoinDto;
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
@Builder
@Entity
@Table(name = "users")
public class User extends Artist {

    @Email
    private String email;

    private String password;
    private String name;
    private Gender gender;
    private UserStatus status;

    public void modifyPassword(String password) {
        this.password = password;
    }

    public static User of(JoinDto joinDto) {
        User newUser = User.builder()
                .email(joinDto.getEmail())
                .password(joinDto.getPassword())
                .name(joinDto.getName())
                .gender(joinDto.getGender())
                .status(UserStatus.ACTIVE)
                .build();

        newUser.modifyBirth(joinDto.getBirth());

        newUser.modifyRegion(joinDto.getRegion());
        newUser.modifyGenre(joinDto.getGenre());
        newUser.modifyPosition(joinDto.getPosition());

        newUser.modifyNickname(joinDto.getNickname());
//        newUser.modifyProfileImage(joinDto.getProfileImage());

        return newUser;
    }
}