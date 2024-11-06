package com.e106.reco.domain.artist.crew.dto;

import com.e106.reco.domain.artist.crew.entity.Crew;
import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.user.dto.UserSummaryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Setter
@Getter
public class CrewInfoDto {
    private Long crewSeq;
    private String nickname;
    private UserSummaryDto manager;
    private List<UserSummaryDto> crews;

    private LocalDate birth;
    private String region;
    private String genre;
    private String content;

    private String profileImage;

    static public CrewInfoDto of (Crew crew, List<CrewUser> crewUsers) {
        return CrewInfoDto.builder()
                .crewSeq(crew.getSeq())
                .manager(UserSummaryDto.of(crew.getManager()))
                .nickname(crew.getNickname())
                .content(crew.getContent())
                .birth(crew.getBirth())
                .region(crew.getRegion().name())
                .genre(crew.getGenre().name())
                .profileImage(crew.getProfileImage())
                .crews(crewUsers.stream().map(crewUser -> UserSummaryDto.of(crewUser.getUser())).collect(Collectors.toList()))
                .build();
    }
}
