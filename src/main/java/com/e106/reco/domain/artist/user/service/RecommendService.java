package com.e106.reco.domain.artist.user.service;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.repository.ArtistRepository;
import com.e106.reco.domain.artist.user.dto.node.ArtistRecommendationDTO;
import com.e106.reco.domain.artist.user.dto.node.ArtistRecommendationProjection;
import com.e106.reco.domain.artist.user.dto.node.InitialRecommendationDTO;
import com.e106.reco.domain.artist.user.dto.node.InitialRecommendationProjection;
import com.e106.reco.domain.artist.user.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {
    private final RecommendRepository recommendRepository;
    private final ArtistRepository artistRepository;
    public List<InitialRecommendationDTO> getInitialRecommendations(Long artistSeq, int limit) {
        return recommendRepository.findInitialRecommendations(artistSeq, limit).stream()
                        .map(projection -> {
                            Long seq = projection.getArtistSeq();
                            Artist artist = artistRepository.findById(seq).orElse(null);
                            assert artist != null;
                            return convertToDTO(projection, artist.getProfileImage());
                        })
                        .toList();
//        return recommendations;
    }
    public List<ArtistRecommendationDTO> getRecommendations(Long artistSeq, int limit) {
        List<ArtistRecommendationProjection> recommendations =
                recommendRepository.findRecommendationsBasedOnFollowing(artistSeq, limit);

        return recommendations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private InitialRecommendationDTO convertToDTO(InitialRecommendationProjection projection, String profileImage) {
        return InitialRecommendationDTO.builder()
                .name(projection.getName())
                .artistSeq(projection.getArtistSeq())
                .genre(projection.getGenre())
                .position(projection.getPosition())
                .profileImage(profileImage)
                .region(projection.getRegion())
                .similarityScore(projection.getSimilarityScore())
                .build();
    }
//    private InitialRecommendationDTO convertToInitDTO(InitialRecommendationProjection projection) {
//        return InitialRecommendationDTO.builder()
//                .name(projection.getName())
//                .similarityScore(normalizeSimilarityScore(projection.getSimilarityScore()))
//                .recommendationReason(generateRecommendationReason(projection))
//                .matchDetails(generateMatchDetails(projection))
//                .build();
//    }


    private ArtistRecommendationDTO convertToDTO(ArtistRecommendationProjection projection) {
        return ArtistRecommendationDTO.builder()
                .artistName(projection.getArtistName())
                .similarity(normalizeSimilarityScore(projection.getSimilarityScore()))
                .recommendationReason(generateRecommendationReason(projection))
                .sharedGenres(projection.getSharedGenres())
                .sharedInstruments(projection.getSharedInstruments())
                .sharedRegions(projection.getSharedRegions())
                .build();
    }

    private double normalizeSimilarityScore(double rawScore) {
        // 최대 가능 점수로 정규화 (장르 2점 + 악기 1점 + 지역 3점)
        return Math.min(rawScore / 20.0, 1.0);  // 20점을 최대치로 가정
    }

    private String generateRecommendationReason(ArtistRecommendationProjection projection) {
        List<String> reasons = new ArrayList<>();

        if (!projection.getSharedGenres().isEmpty()) {
            reasons.add(String.format("팔로우하는 아티스트와 %s 장르를 공유합니다",
                    String.join(", ", projection.getSharedGenres())));
        }

        if (!projection.getSharedInstruments().isEmpty()) {
            reasons.add(String.format("%s 악기를 다룹니다",
                    String.join(", ", projection.getSharedInstruments())));
        }

        if (!projection.getSharedRegions().isEmpty()) {
            reasons.add(String.format("%s 지역에서 활동합니다",
                    String.join(", ", projection.getSharedRegions())));
        }

        return String.join(". ", reasons);
    }

    private String generateRecommendationReason(InitialRecommendationProjection projection) {
        List<String> reasons = new ArrayList<>();

        // 지역 매칭 (3점)
        if (projection.getArtistRegion() != null) {
            reasons.add(String.format("같은 %s 지역에서 활동", projection.getArtistRegion()));
        }

        // 장르 매칭 (2점)
        if (!projection.getSharedGenres().isEmpty()) {
            reasons.add(String.format("공통 장르: %s", String.join(", ", projection.getSharedGenres())));
        }

        // 악기 매칭 (1점)
        if (!projection.getSharedInstruments().isEmpty()) {
            reasons.add(String.format("공통 악기: %s", String.join(", ", projection.getSharedInstruments())));
        }

        return String.join(" • ", reasons);
    }

    private Map<String, Object> generateMatchDetails(InitialRecommendationProjection projection) {
        Map<String, Object> details = new HashMap<>();
        details.put("region", projection.getArtistRegion());
        details.put("genres", projection.getSharedGenres());
        details.put("instruments", projection.getSharedInstruments());
        details.put("rawScore", projection.getSimilarityScore());
        return details;
    }
}
