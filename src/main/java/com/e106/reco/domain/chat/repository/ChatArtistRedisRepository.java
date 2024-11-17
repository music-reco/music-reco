package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.chat.entity.ChatArtist;
import com.e106.reco.domain.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatArtistRedisRepository {

    private final String PREFIX_CHAT = "chatArtist:";
    private final int LIMIT_TIME = 5 * 60 * 24 * 30;

    private final ReactiveRedisTemplate<String, ChatArtist> chatArtistRedisTemplate;
    private final ChatArtistMongoRepository chatArtistMongoRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Mono<ChatArtist> getChatUser(Artist artist, Long roomSeq) {
        String redisKey = PREFIX_CHAT + artist.getSeq();

        // Redis에서 데이터 조회 후 없으면 MongoDB에서 조회하여 Redis에 캐싱
        return chatArtistRedisTemplate.opsForValue()
                .get(redisKey)
                .switchIfEmpty(chatArtistMongoRepository.findByArtistSeqAndRoomSeq(artist.getSeq(), roomSeq)
                        .switchIfEmpty(
                                Mono.defer(()-> {
                                            ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().artistSeq(artist.getSeq()).roomSeq(roomSeq).build())
                                                    .orElse(null);

                                            ChatArtist chatArtist = ChatArtist.of(artist, roomSeq, chatRoom.getJoinAt());
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
    public Mono<ChatArtist> createChatUser(Artist artist, Long roomSeq){
        String redisKey = PREFIX_CHAT + artist.getSeq();

        ChatArtist chatArtist = chatArtistMongoRepository.findByArtistSeqAndRoomSeq(artist.getSeq(), roomSeq).block();
        if(Objects.isNull(chatArtist)){
            ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().artistSeq(artist.getSeq()).roomSeq(roomSeq).build())
                    .orElse(null);

            chatArtist = ChatArtist.of(artist, roomSeq, chatRoom.getJoinAt());
        }
        chatArtistRedisTemplate.opsForValue()
                .set(redisKey,chatArtist, Duration.ofSeconds(LIMIT_TIME))
                .subscribe();
        return Mono.just(chatArtist);
    }
}
