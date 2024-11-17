package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.chat.entity.ChatArtist;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ChatArtistMongoRepository extends ReactiveMongoRepository<ChatArtist, String> {

    @Query("{ 'artistSeq': ?0 , 'roomSeq' : ?1}")
    Mono<ChatArtist> findByArtistSeqAndRoomSeq(Long artistSeq, Long roomSeq);
}