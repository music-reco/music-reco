package com.e106.reco.domain.artist.user.entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("활성"),
    SLEEP("휴면"),
    INACTIVE("비활성");

    private final String state;

}