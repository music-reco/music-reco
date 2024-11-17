//package com.e106.reco.domain.chat.repository;
//
//import com.e106.reco.domain.artist.entity.Artist;
//import com.e106.reco.domain.artist.repository.ArtistRepository;
//import com.e106.reco.domain.chat.entity.ChatArtist;
//import com.e106.reco.domain.chat.entity.ChatRoom;
//import com.e106.reco.global.error.exception.BusinessException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//
//import static com.e106.reco.global.error.errorcode.ArtistErrorCode.ARTIST_NOT_FOUND;
//
//@Repository
//@RequiredArgsConstructor
//@Slf4j
//public class ChatArtistStateRepository {
//    private final String PREFIX_ARTIST = "Artist:"; // key값이 중복되지 않도록 상수 선언
//    private final String PREFIX_ROOM = "/Room:";
//    private final int LIMIT_TIME = 60*24; // 프로필 유효 시간 1일
//
//    private final RedisTemplate<String, String> joinRepository; // Redis
//    private final ReactiveRedisTemplate<String, ChatArtist> chatArtistRepository;
//
//    private final ArtistRepository artistRepository;
//    private final ChatRoomRepository chatRoomRepository;
//
////    @Scheduled(fixedRate = 5000)
////    public void updateConnectionStatus(Long artistSeq, Long roomSeq) {
////        redisTemplate.opsForValue()
////                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
////
////
////    }
//
//    public void createJoinChatUserState(Long artistSeq, Long roomSeq) {
//        Artist artist = artistRepository.findById(artistSeq).orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));
//        ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().artistSeq(artist.getSeq()).roomSeq(roomSeq).build())
//                .orElse(null);
//
//        ChatArtist chatArtist = ChatArtist.of(artist, roomSeq, chatRoom.getJoinAt());
//
//
//        chatArtistRepository.opsForValue().set(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq
//                , chatArtist
//                , Duration.ofSeconds(LIMIT_TIME));
//    }
//
//    public Mono<ChatArtist> getJoinChatUserState(Long artistSeq, Long roomSeq) {
//        if (joinRepository.hasKey(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq).equals(Boolean.FALSE)) {
//            createJoinChatUserState(artistSeq, roomSeq);
//        }
//        return chatArtistRepository.opsForValue()
//                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
//    }
//
//}
