//package com.e106.reco.domain.chat.repository;
//
//import com.e106.reco.domain.chat.entity.ChatArtist;
//import com.mongodb.client.result.UpdateResult;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;
//
//@Repository
//@RequiredArgsConstructor
//@Slf4j
//public class ChatArtistStateRepository {
//    private final ReactiveMongoTemplate reactiveMongoTemplate;
//
//    public Mono<UpdateResult> updateJoinAtToNull(Long artistSeq, Long roomSeq) {
//        return reactiveMongoTemplate.updateFirst(
//                Query.query(Criteria.where("_id.artistSeq").is(artistSeq).and("_id.roomSeq").is(roomSeq)), // 검색 조건
//                Update.update("joinAt", null), // joinAt 필드를 null로 업데이트
//                ChatArtist.class // 업데이트할 엔티티 클래스
//        );
//    }
//
//}
