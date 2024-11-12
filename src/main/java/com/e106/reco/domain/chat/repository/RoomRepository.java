package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>{
    Optional<Room> findBySeq(Long Seq);
}
