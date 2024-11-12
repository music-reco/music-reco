package com.e106.reco.domain.artist.controller;

import com.e106.reco.domain.artist.crew.repository.CrewRepository;
import com.e106.reco.domain.artist.dto.ArtistSummaryDto;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.user.dto.UserSummaryDto;
import com.e106.reco.domain.artist.user.repository.UserRepository;
import com.e106.reco.domain.board.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/artists")
public class ArtistController {
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final CrewRepository crewRepository;

    @GetMapping("/{word}")
    public List<ArtistSummaryDto> getArtists(@PathVariable String word) {

        return artistRepository.findByNickname(word)
                .stream().map(artist -> ArtistSummaryDto.builder()
                        .nickname(artist.getNickname())
                        .profileImage(artist.getNickname())
                        .position(artist.getPosition())
                        .seq(artist.getSeq())
                        .email(artist.getPosition() != Position.CREW ? userRepository.findBySeq(artist.getSeq()).get().getEmail() : null)
                        .manage(artist.getPosition() != Position.CREW ? null : UserSummaryDto.of(crewRepository.findBySeq(artist.getSeq()).get().getManager()))
                        .build()
                ).toList();
    }
}
