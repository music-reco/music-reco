package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.chat.entity.ChatArtist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatArtistRedisRepository {

    private final String PREFIX_CHAT = "chatArtist:";
    private final int LIMIT_TIME = 5 * 60 * 24 * 30;

    private final ReactiveRedisTemplate<String, ChatArtist> chatArtistRedisTemplate;
    private final ChatArtistMongoRepository chatArtistMongoRepository;

    public Mono<ChatArtist> getChatUser(Artist artist, Long roomSeq) {
        String redisKey = PREFIX_CHAT + artist.getSeq();

        // Redis에서 데이터 조회 후 없으면 MongoDB에서 조회하여 Redis에 캐싱
        return chatArtistRedisTemplate.opsForValue()
                .get(redisKey)
                .switchIfEmpty(chatArtistMongoRepository.findByArtistSeq(artist.getSeq().toString(), roomSeq.toString())
                        .switchIfEmpty(
                                Mono.defer(()-> {
                                            ChatArtist chatArtist = ChatArtist.of(artist);
                                            return chatArtistMongoRepository.save(chatArtist)
                                                    .then(Mono.just(chatArtist));
                                        }
                                )
                        )
                        .doOnNext(chatArtist ->
                                chatArtistRedisTemplate.opsForValue()
                                        .set(redisKey, chatArtist, Duration.ofSeconds(LIMIT_TIME))
                                        .subscribe()
                        )
                );
    }
    public Mono<ChatArtist> createChatUser(Artist artist){
        String redisKey = PREFIX_CHAT + artist.getSeq();
        ChatArtist chatArtist = ChatArtist.of(artist);
        chatArtistMongoRepository.save(chatArtist);
        chatArtistRedisTemplate.opsForValue()
                .set(redisKey,chatArtist, Duration.ofSeconds(LIMIT_TIME))
                .subscribe();
        return Mono.just(chatArtist);
    }
}
