package com.e106.reco.domain.chat.repository;//package com.e106.reco.domain.chat.repository;
//
//import com.e106.reco.domain.board.repository.ArtistRepository;
//import com.e106.reco.domain.chat.entity.ChatArtistState;
//import com.e106.reco.domain.chat.entity.ChatRoom;
//import com.e106.reco.global.error.exception.BusinessException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//import static com.e106.reco.global.error.errorcode.ChatErrorCode.ROOM_NOT_FOUND;
//
//@Repository
//@RequiredArgsConstructor
//@Slf4j
//public class ChatArtistStateRepository {
//    private final String PREFIX_ARTIST = "Artist:"; // key값이 중복되지 않도록 상수 선언
//    private final String PREFIX_ROOM = "/Room:";
//    private final int LIMIT_TIME = 60*24; // 프로필 유효 시간 1일
//
//    private final ReactiveRedisTemplate<String, String> redisTemplate; // Redis
//    private final ArtistRepository artistRepository;
//    private final ChatRoomRepository chatRoomRepository;
//
//    @Scheduled(fixedRate = 5000)
//    public void updateConnectionStatus(Long artistSeq, Long roomSeq) {
//        redisTemplate.opsForValue()
//                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
//
//
//    }
//
//    public void createChatUserState(Long artistSeq, Long roomSeq) {
//        ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().roomSeq(roomSeq).artistSeq(artistSeq).build())
//                .orElseThrow(()-> new BusinessException(ROOM_NOT_FOUND));
//        chatRoom.updateLastActiveAt();
//
//        redisTemplate.opsForValue().set(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq,
//                ChatArtistState.builder()
//                            .joinTime(LocalDateTime.now())
//                            .lastOnlineTime(LocalDateTime.now())
//                            .build(),
//                Duration.ofSeconds(LIMIT_TIME));
//    }
//    public Mono<ChatArtistState> getChatUserState(Long artistSeq, Long roomSeq) {
//        if (redisTemplate.hasKey(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq).equals(Boolean.FALSE)) {
//            createChatUserState(artistSeq, roomSeq);
//        }
//        return redisTemplate.opsForValue()
//                .get(PREFIX_ARTIST + artistSeq + PREFIX_ROOM + roomSeq);
//    }
//
//}
