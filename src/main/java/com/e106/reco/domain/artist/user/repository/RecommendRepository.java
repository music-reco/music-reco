package com.e106.reco.domain.artist.user.repository;

import com.e106.reco.domain.artist.user.dto.node.ArtistRecommendation;
import com.e106.reco.domain.artist.user.dto.node.ArtistRecommendationProjection;
import com.e106.reco.domain.artist.user.dto.node.GenreStatistics;
import com.e106.reco.domain.artist.user.dto.node.InitialRecommendationDTO;
import com.e106.reco.domain.artist.user.node.ArtistNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendRepository extends Neo4jRepository<ArtistNode, Long> {
    @Query("""
    // 1. 내가 팔로우하는 아티스트들 찾기
    MATCH (me:Artist {artistSeq: $artistSeq})-[:FOLLOWS]->(followed:Artist)
    
    // 2. 팔로우하는 아티스트들의 장르, 악기, 지역 정보 수집
    MATCH (followed)-[:PLAYS_GENRE]->(g:Genre)
    MATCH (followed)-[:PLAYS_INSTRUMENT]->(i:Instrument)
    MATCH (followed)-[:BASED_IN]->(r:Region)
    
    // 3. 유사한 아티스트 찾기 (내가 팔로우하지 않는)
    MATCH (other:Artist)
    WHERE NOT (me)-[:FOLLOWS]->(other)
    AND other <> me
    
    // 4. 유사도 계산
    OPTIONAL MATCH (other)-[:PLAYS_GENRE]->(og:Genre)
    WHERE g = og
    WITH other, followed, g, i, r, 
         count(DISTINCT og) as genreOverlap
    
    OPTIONAL MATCH (other)-[:PLAYS_INSTRUMENT]->(oi:Instrument)
    WHERE i = oi
    WITH other, followed, g, r,
         genreOverlap,
         count(DISTINCT oi) as instrumentOverlap
    
    OPTIONAL MATCH (other)-[:BASED_IN]->(or:Region)
    WHERE r = or
    
    // 5. 최종 유사도 스코어 계산
    WITH other,
         sum(genreOverlap) * 2.0 + 
         sum(instrumentOverlap) * 1.0 +
         count(DISTINCT r) * 3.0 as similarityScore,
         collect(DISTINCT g.name) as sharedGenres,
         collect(DISTINCT i.name) as sharedInstruments,
         collect(DISTINCT r.name) as sharedRegions
    
    // 6. 결과 반환
    RETURN other.name as artistName,
           similarityScore,
           sharedGenres,
           sharedInstruments,
           sharedRegions
    ORDER BY similarityScore DESC
    LIMIT $limit
    """)
    List<ArtistRecommendationProjection> findRecommendationsBasedOnFollowing(
            @Param("artistSeq") Long artistSeq,
            @Param("limit") int limit
    );

    @Query("""
    // 1. 현재 아티스트의 정보 가져오기
    MATCH (me:Artist {artistSeq: $artistSeq})
    MATCH (me)-[:PLAYS_GENRE]->(myGenre:Genre)
    MATCH (me)-[:PLAYS_INSTRUMENT]->(myInstrument:Instrument)
    MATCH (me)-[:BASED_IN]->(myRegion:Region)

    // 2. 다른 아티스트 찾기 (자신 제외)
    WITH me, myGenre, myInstrument, myRegion
    MATCH (other:Artist)
    WHERE other <> me

    // 3. 지역 매칭 (3점)
    OPTIONAL MATCH (other)-[:BASED_IN]->(otherRegion:Region)
    WHERE otherRegion = myRegion
    WITH me, other, myGenre, myInstrument, otherRegion,
         CASE WHEN otherRegion IS NOT NULL THEN 3.0 ELSE 0.0 END as regionScore

    // 4. 장르 매칭 (2점)
    OPTIONAL MATCH (other)-[:PLAYS_GENRE]->(otherGenre:Genre)
    WHERE otherGenre = myGenre
    WITH me, other, myInstrument, regionScore, otherGenre, otherRegion,
         CASE WHEN count(otherGenre) > 0 THEN 2.0 ELSE 0.0 END as genreScore,
         collect(DISTINCT otherGenre.name) as matchedGenres

    // 5. 악기 매칭 (1점)
    OPTIONAL MATCH (other)-[:PLAYS_INSTRUMENT]->(otherInstrument:Instrument)
    WHERE otherInstrument = myInstrument
    WITH other, regionScore, genreScore, matchedGenres, otherInstrument, otherRegion,
         CASE WHEN count(otherInstrument) > 0 THEN 1.0 ELSE 0.0 END as instrumentScore,
         collect(DISTINCT otherInstrument.name) as matchedInstruments

    // 6. 최종 점수 계산 및 결과 반환
    WITH other.name as name,
     other.artistSeq as artistSeq,
     regionScore + genreScore + instrumentScore as totalScore,
     matchedGenres as genre,
     matchedInstruments as position,
     otherRegion.name as region
    WHERE totalScore > 0
    WITH totalScore, collect({name: name, artistSeq: artistSeq}) as artists
    UNWIND artists as artist
    WITH artist.name as name,
         artist.artistSeq as artistSeq,
         totalScore as similarityScore,
         rand() as random
    RETURN name, similarityScore, artistSeq
    ORDER BY similarityScore DESC, random
    LIMIT $limit""")
    List<InitialRecommendationDTO> findInitialRecommendations(
            @Param("artistSeq") Long artistSeq,
            @Param("limit") int limit
    );


    @Query("MATCH (a:Artist {name: $artistName}) " +
            "MATCH (a)-[:PLAYS_GENRE]->(g:Genre)<-[:PLAYS_GENRE]-(other:Artist) " +
            "WHERE a <> other " +
            "WITH other, count(g) as genreOverlap " +
            "MATCH (other)-[:PLAYS_INSTRUMENT]->(i:Instrument) " +
            "RETURN other, genreOverlap " +
            "ORDER BY genreOverlap DESC " +
            "LIMIT $limit")
    List<ArtistRecommendation> findSimilarArtists(Long artistSeq, int limit);

    Optional<ArtistNode> findByName(String name);

    @Query("MATCH (a:Artist)-[:PLAYS_GENRE]->(g:Genre) " +
            "RETURN g.name as genre, count(a) as artistCount " +
            "ORDER BY artistCount DESC")
    List<GenreStatistics> getGenreStatistics();
}