package com.e106.reco.domain.chat.service;

import com.e106.reco.domain.artist.crew.entity.CrewUser;
import com.e106.reco.domain.artist.crew.entity.CrewUserState;
import com.e106.reco.domain.artist.crew.repository.CrewUserRepository;
import com.e106.reco.domain.artist.entity.Artist;
import com.e106.reco.domain.artist.entity.Position;
import com.e106.reco.domain.artist.repository.ArtistRepository;
import com.e106.reco.domain.chat.dto.ChatRoomResponse;
import com.e106.reco.domain.chat.dto.RoomResponse;
import com.e106.reco.domain.chat.entity.Chat;
import com.e106.reco.domain.chat.entity.ChatArtist;
import com.e106.reco.domain.chat.entity.ChatRoom;
import com.e106.reco.domain.chat.entity.Room;
import com.e106.reco.domain.chat.repository.ChatArtistMongoRepository;
import com.e106.reco.domain.chat.repository.ChatArtistRedisRepository;
import com.e106.reco.domain.chat.repository.ChatRepository;
import com.e106.reco.domain.chat.repository.ChatRoomRepository;
import com.e106.reco.domain.chat.repository.RoomRepository;
import com.e106.reco.global.error.exception.BusinessException;
import com.e106.reco.global.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.e106.reco.global.error.errorcode.ArtistErrorCode.ARTIST_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.ChatErrorCode.CHAT_GRANT_FAIL;
import static com.e106.reco.global.error.errorcode.ChatErrorCode.ROOM_NOT_FOUND;
import static com.e106.reco.global.error.errorcode.CrewErrorCode.CREW_USER_NOT_FOUND;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {
    private final RoomRepository roomRepository;
    private final ArtistRepository artistRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CrewUserRepository crewUserRepository;
    private final ChatArtistRedisRepository chatArtistRedisRepository;
//    private final ChatArtistStateRepository chatArtistStateRepository;
    private final ChatArtistMongoRepository chatArtistMongoRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public Flux<Chat> getMsg(Long artistSeq, Long roomSeq){
        Artist artist = artistRepository.findBySeq(artistSeq).orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));
        AuthUtil.getWebfluxCustomUserDetails()
                .subscribe(user -> artistCertification(user.getSeq(), artist));

        return chatArtistMongoRepository.findByArtistSeqAndRoomSeq(artistSeq, roomSeq)
                .flatMapMany(chatArtist ->
                        chatRepository.mFindByGroupSeqAfterJoin(roomSeq.toString(),chatArtist.getJoinAt())
                                .subscribeOn(Schedulers.boundedElastic())
                );


    }

    public void invite(Long artistSeq, Long roomSeq){
        Room room = roomRepository.findBySeq(roomSeq).orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));

        Artist artist = artistRepository.findBySeq(artistSeq)
                .orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));

        LocalDateTime joinTime = LocalDateTime.now();

        ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(artistSeq).build())
                .orElse(null);

//        ChatArtist chatArtist = chatArtistMongoRepository.findByArtistSeqAndRoomSeq(artistSeq, roomSeq)
//                .switchIfEmpty(null)
//                .block();
        // 수정한 부분
        ChatArtist chatArtist = chatArtistMongoRepository.findByArtistSeqAndRoomSeq(artistSeq, roomSeq)
                .switchIfEmpty(Mono.empty())  // null 대신 Mono.empty() 사용
                .block();

        if(Objects.isNull(chatRoom) || Objects.isNull(chatArtist)) {
            chatRoom = ChatRoom.builder()
                    .room(room)
                    .artist(artist)
                    .pk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(artistSeq).build())
                    .joinAt(joinTime)
                    .build();
            chatArtist = ChatArtist.of(artist, roomSeq, joinTime);
        }else if (Objects.isNull(chatRoom.getJoinAt()) || Objects.isNull(chatArtist.getJoinAt())){
            chatArtist.join(joinTime);
            chatRoom.joinChatRoom(joinTime);
        }
        chatRoomRepository.save(chatRoom);
        chatArtistMongoRepository.save(chatArtist).block();
    }
public Flux<RoomResponse> getChatRooms(Long artistSeq) {
    Artist artist = artistRepository.findBySeq(artistSeq)
            .orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));

    // 인증을 수행하지만, subscribe()를 하므로 비동기적 처리임에 유의
    AuthUtil.getWebfluxCustomUserDetails()
            .subscribe(user -> artistCertification(user.getSeq(), artist));

    // 방 목록을 비동기 Flux로 변환
    List<Room> rooms = chatRoomRepository.findExistRoomsByArtistSeq(artistSeq);

    rooms.stream().forEach(room -> log.info("rooms: {}", room.getSeq()));

    return Flux.fromIterable(rooms)
            .flatMap(room -> chatRepository.findTopByRoomSeqOrderByCreatedAtDesc(room.getSeq().toString())
                    .map(lastChat -> RoomResponse.builder()
                            .roomSeq(room.getSeq())
                            .lastMsg(lastChat != null ? lastChat.getMsg() : "No messages yet")
                            .lastMsgTime(lastChat != null ? lastChat.getCreatedAt() : null)
                            .chatRoomResponses(chatRoomRepository.findExistNameRoomsByRoomSeq(room.getSeq())
                                    .stream()
                                    .filter(chatRoomArtist -> !artistSeq.equals(chatRoomArtist.getSeq()))
                                    .map(ChatRoomResponse::of)
                                    .toList()
                            )
                            .build()
                    )
            );
}


//    public Flux<ChatArtist> getArtistInfo(Long roomSeq) {
//        // roomSeq에 해당하는 artistSeq 리스트 조회
//        List<Artist> artistList = chatRoomRepository.artistFindByRoomSeq(roomSeq);
//
//        // 각 artistSeq에 대해 Redis에서 ChatArtist 조회
//        return Flux.fromIterable(artistList) // List<Long>을 Flux<Long>으로 변환
//                .flatMap(chatArtistRedisRepository::getChatUser)
//                .repeatWhen(artistUpdates -> artistUpdates.delayElements(Duration.ofSeconds(5))) // 5초마다 반복
//                .distinctUntilChanged()
//                .subscribeOn(Schedulers.boundedElastic());
//    }


//    public void leave(Long roomSeq, Long artistSeq) {
//        CustomUserDetails customUserDetails = AuthUtil.getWebfluxCustomUserDetails().block();
//        Artist artist = artistRepository.findBySeq(artistSeq).orElseThrow(()->new BusinessException(ARTIST_NOT_FOUND));
//        artistCertification(customUserDetails.getSeq(), artist);
//
//        ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().roomSeq(roomSeq).artistSeq(artistSeq).build())
//                .orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));
//
//        if(chatRoom.getState() == RoomState.INACTIVE)
//            throw new BusinessException(ARTIST_NOT_IN_CHAT);
//
//        chatRoom.leaveChatRoom();
//    }
//    public void invite(Long roomSeq, Long artistSeq){
//        CustomUserDetails customUserDetails = AuthUtil.getWebfluxCustomUserDetails().block();
//
//        Room room = roomRepository.findBySeq(roomSeq).orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));
//
//        Artist artist = artistRepository.findBySeq(artistSeq)
//                .orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));
//
////        if(chatRoom.getState() == RoomState.PERSONAL) //TODO : 유저의 joinTime 병경
//
//        if(artist.getPosition().equals(Position.CREW)) throw new BusinessException(CHAT_NOT_ALLOW_GROUP_CHAT);
//
//        if(chatRoomRepository.countByRoom(room)==2) {
//            List<Long> receivers = chatRoomRepository.artistSeqFindByRoomSeq(roomSeq);
//            Long newRoomSeq = createGroupChatRoom(RoomRequest.builder()
//                    .senderSeq(customUserDetails.getSeq())
//                    .receiversSeq(receivers)
//                    .build());
//            room = roomRepository.findBySeq(newRoomSeq)
//                    .orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));
//        }
//
//        ChatRoom chatRoom = chatRoomRepository.findByPk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(artistSeq).build())
//                .orElse(ChatRoom.builder().room(room)
//                                        .artist(artist)
//                                        .pk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(artistSeq).build())
//                        .build());
//        chatRoom.joinChatRoom();
//        chatArtistStateRepository.createJoinChatUserState(artistSeq, roomSeq);
//        chatRoomRepository.save(chatRoom);
//    }
    public Mono<Chat> sendMsg(Chat chat){
        Artist artist = artistRepository.findBySeq(Long.parseLong(chat.getArtistSeq()))
            .orElseThrow(()->new BusinessException(ARTIST_NOT_FOUND));

        AuthUtil.getWebfluxCustomUserDetails()
                .subscribe(user -> artistCertification(user.getSeq(), artist));

        invite(Long.parseLong(chat.getArtistSeq()), Long.parseLong(chat.getRoomSeq()));

        chat.setCreatedAt(LocalDateTime.now());

        return chatRepository.save(chat);
    }
//    public Long createSingleChatRoom(RoomRequest roomRequest) {
//        CustomUserDetails user = AuthUtil.getWebfluxCustomUserDetails().block();
//
//        if(roomRequest.getReceiversSeq()==null || roomRequest.getReceiversSeq().size()!=1) throw new BusinessException(SINGLE_CHAT_ONLY_ONE_RECEIVER);
//
//
//        Artist sender = artistRepository.findBySeq(roomRequest.getSenderSeq())
//                        .orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));
//        artistCertification(user.getSeq(), sender);
//        Artist receiver = artistRepository.findBySeq(roomRequest.getReceiversSeq().getFirst())
//                        .orElseThrow(()-> new BusinessException(ARTIST_NOT_FOUND));
//
//        Room room = chatRoomRepository.findPersonalChatRoomBetweenArtists(roomRequest.getSenderSeq(), roomRequest.getReceiversSeq().getFirst())
//                .orElse(roomRepository.save(Room.builder().build()));
//
//        chatRoomRepository.save(ChatRoom.builder()
//                        .artist(sender)
//                        .room(room)
//                        .joinAt(LocalDateTime.now())
//                        .pk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(sender.getSeq()).build())
//                        .state(RoomState.PERSONAL)
//                        .build());
//        chatRoomRepository.save(ChatRoom.builder()
//                        .artist(receiver)
//                        .room(room)
//                        .joinAt(LocalDateTime.now())
//                        .pk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(receiver.getSeq()).build())
//                        .state(RoomState.PERSONAL)
//                        .build());
//        return room.getSeq();
//    }
//    public Long createGroupChatRoom(RoomRequest roomRequest){
//        CustomUserDetails user = AuthUtil.getWebfluxCustomUserDetails().block();
//
//        Artist sender = artistRepository.findBySeq(roomRequest.getSenderSeq())
//                .orElseThrow(() -> new BusinessException(ARTIST_NOT_FOUND));
//        artistCertification(user.getSeq(), sender);
//
//        Room room = roomRepository.save(Room.builder().build());
//
//        List<ChatRoom> chatRooms = new ArrayList<>(Arrays.asList(
//                ChatRoom.builder()
//                .artist(sender)
//                .room(room)
//                .joinAt(LocalDateTime.now())
//                .pk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(sender.getSeq()).build())
//                .state(RoomState.ACTIVE)
//                .build()));
//        for(Long receiver : roomRequest.getReceiversSeq()){
//            Artist artist = artistRepository.findBySeq(receiver)
//                    .orElseThrow(()->new BusinessException(ARTIST_NOT_FOUND));
//            if(artist.getPosition().equals(Position.CREW)) throw new BusinessException(CHAT_NOT_ALLOW_GROUP_CHAT);
//            chatRooms.add(ChatRoom.builder()
//                            .artist(artist)
//                            .room(room)
//                            .joinAt(LocalDateTime.now())
//                            .pk(ChatRoom.PK.builder().roomSeq(room.getSeq()).artistSeq(artist.getSeq()).build())
//                            .state(RoomState.ACTIVE)
//                            .build());
//        }
//        chatRoomRepository.saveAll(chatRooms);
//        return room.getSeq();
//    }
    public Flux<ChatArtist> getArtistInfo(Long roomSeq) {
        // 필터링 조건: 특정 roomSeq에 해당하는 ChatArtist 변경사항만
        Criteria criteria = where("roomSeq").is(roomSeq);

        // 먼저, 해당 roomSeq에 대한 모든 ChatArtist를 반환
        Flux<ChatArtist> chatArtists = Flux.fromIterable(chatRoomRepository.artistFindByRoomSeq(roomSeq)
                .stream().map(artist -> ChatArtist.of(artist)).toList());


        // Change Stream을 활용해 변경사항 스트리밍
        Flux<ChatArtist> changeStream = Flux.create(sink -> {
            mongoTemplate.changeStream(ChatArtist.class)
                    .filter(criteria)  // roomSeq 필터
                    .listen()  // ChangeStream을 통해 변화 감지
                    .doOnTerminate(sink::complete)  // 종료 시 처리
                    .subscribe(changeStreamEvent -> {
                        // ChangeStreamEvent에서 getBody()를 사용하여 실제 변경된 ChatArtist 객체를 가져옵니다.
                        ChatArtist updatedChatArtist = changeStreamEvent.getBody();
                        if (updatedChatArtist != null) {
                            sink.next(updatedChatArtist);  // 변경된 ChatArtist 객체를 반환
                        }
                    });
        });

        // initialData와 changeStream을 합쳐서 반환
        return chatArtists.concatWith(changeStream);
    }



    private void artistCertification(Long userSeq, Artist artist) {
        if( artist.getPosition() == Position.CREW ) {
            CrewUser crewUser = crewUserRepository.findCrewUserByPk(CrewUser.PK.builder()
                            .crewSeq(artist.getSeq())
                            .userSeq(userSeq)
                            .build())
                    .orElseThrow(()-> new BusinessException(CREW_USER_NOT_FOUND));

            if(crewUser.getState() != CrewUserState.ALL && crewUser.getState() != CrewUserState.CHAT)
                throw new BusinessException(CHAT_GRANT_FAIL);
        } else if(!artist.getSeq().equals(userSeq)) throw new BusinessException(CHAT_GRANT_FAIL);
    }
}
