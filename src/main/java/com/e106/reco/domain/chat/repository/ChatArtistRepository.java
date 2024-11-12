package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.board.repository.ArtistRepository;
import com.e106.reco.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.e106.reco.global.error.errorcode.ArtistErrorCode.ARTIST_NOT_FOUND;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatArtistRepository {
    private final String PREFIX_CHAT = "chatArtist:"; // key값이 중복되지 않도록 상수 선언
    private final int LIMIT_TIME = 5*60*24*30; // 프로필 유효 시간 5개월

    private final ReactiveRedisTemplate<String, String> chatArtistRedisTemplate; // Redis

    private final ArtistRepository artistRepository;

    public void createChatUser(Artist artist) {
        chatArtistRedisTemplate.opsForValue().set(PREFIX_CHAT + artist.getSeq(),
                artist.getProfileImage()+"\n"+artist.getNickname(),
                Duration.ofSeconds(LIMIT_TIME));
    }

    public Mono<String> getChatUser(String artistSeq) {
        if (chatArtistRedisTemplate.hasKey(PREFIX_CHAT + artistSeq).equals(Boolean.FALSE)) {
            Artist artist = artistRepository.findBySeq(Long.parseLong(artistSeq))
                    .orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));
            createChatUser(artist);
        }
        return chatArtistRedisTemplate.opsForValue()
                .get(PREFIX_CHAT + artistSeq);
    }

}
