package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.chat.entity.ChatArtist;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ChatArtistMongoRepository extends ReactiveMongoRepository<ChatArtist, ChatArtist.PK> {

    @Query("{ '_id.artistSeq': ?0 , '_id.roomSeq': ?1 }")
    Mono<ChatArtist> findByArtistSeqAndRoomSeq(Long artistSeq, Long roomSeq);

}