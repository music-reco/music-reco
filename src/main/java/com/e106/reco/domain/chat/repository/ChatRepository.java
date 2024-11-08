package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
//    @Query("{sender:?0, receiver:?1}")
//    Flux<Chat> mFindBySender(String sender, String receiver);
}
