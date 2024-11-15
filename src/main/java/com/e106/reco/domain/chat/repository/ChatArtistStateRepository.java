package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.repository.ArtistRepository;
import com.e106.reco.domain.chat.entity.ChatArtist;
import com.e106.reco.domain.chat.entity.ChatRoom;
import com.e106.reco.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.e106.reco.global.error.errorcode.ArtistErrorCode.ARTIST_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.ChatErrorCode.ARTIST_NOT_IN_CHAT;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatArtistStateRepository {
    private final String PREFIX_ARTIST = "Artist:"; // key값이 중복되지 않도록 상수 선언
    private final String PREFIX_ROOM = "/Room:";
    private final int LIMIT_TIME = 60*24; // 프로필 유효 시간 1일

    private final RedisTemplate<String, String> joinRepository; // Redis
    private final ReactiveRedisTemplate<String, ChatArtist> chatArtistRepository;

    private final ArtistRepository artistRepository;
    private final ChatRoomRepository chatRoomRepository;

//    @Scheduled(fixedRate = 5000)
//    public void updateConnectionStatus(Long artistSeq, Long roomSeq) {
//        redisTemplate.opsForValue()
//                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
//
//
//    }

//    public void createJoinChatUserState(Long artistSeq, Long roomSeq) {
//        joinRepository.opsForValue().set(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq
//                , String.valueOf(chatRoomRepository.findJoinTimeByPk(ChatRoom.PK.builder().roomSeq(roomSeq).artistSeq(artistSeq).build())
//                        .orElseThrow(()-> new BusinessException(ROOM_NOT_FOUND)))
//                ,Duration.ofSeconds(LIMIT_TIME));
//    }

//    public String getJoinChatUserState(Long artistSeq, Long roomSeq) {
//        if (joinRepository.hasKey(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq).equals(Boolean.FALSE)) {
//            createJoinChatUserState(artistSeq, roomSeq);
//        }
//        return joinRepository.opsForValue()
//                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
//    }
    public void createJoinChatUserState(Long artistSeq, Long roomSeq, LocalDateTime joinTime) {
        Artist artist = artistRepository.findById(artistSeq).orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));
        chatArtistRepository.opsForValue().set(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq
                , ChatArtist.of(artist, joinTime)
                , Duration.ofSeconds(LIMIT_TIME));
    }
    public void createJoinChatUserState(Long artistSeq, Long roomSeq) {
        Artist artist = artistRepository.findById(artistSeq).orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));
        chatArtistRepository.opsForValue().set(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq
                , ChatArtist.of(artist,
                        chatRoomRepository.findJoinTimeByPk(ChatRoom.PK.builder().roomSeq(roomSeq).artistSeq(artistSeq).build())
                                                    .orElseThrow(()->new BusinessException(ARTIST_NOT_IN_CHAT)))
                ,Duration.ofSeconds(LIMIT_TIME));
    }

    public Mono<ChatArtist> getJoinChatUserState(Long artistSeq, Long roomSeq) {
        if (joinRepository.hasKey(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq).equals(Boolean.FALSE)) {
            createJoinChatUserState(artistSeq, roomSeq);
        }
        return chatArtistRepository.opsForValue()
                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
    }

}
