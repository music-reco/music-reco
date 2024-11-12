package com.e106.reco.domain.chat.repository;

import com.e106.reco.domain.chat.entity.ChatRoom;
import com.e106.reco.domain.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
    @Query("select cr from ChatRoom cr where cr.pk = :pk")
    Optional<ChatRoom> findByPk(@Param("pk") ChatRoom.PK pk);

    @Query("select count(*) from ChatRoom where room = :room")
    int countByRoom(@Param("room") Room room);

    @Query("select cr.artist.seq from ChatRoom cr where cr.room = :room")
    List<Long> artistSeqFindByRoom(@Param("room") Room room);

    @Query("SELECT cr1.room " +
            "FROM ChatRoom cr1 " +
            "JOIN ChatRoom cr2 ON cr1.room.seq = cr2.room.seq " +
            "WHERE cr1.pk.artistSeq = :artistSeq1 " +
            "AND cr2.pk.artistSeq = :artistSeq2 " +
            "AND cr1.state = com.e106.reco.domain.chat.entity.RoomState.PERSONAL " +
            "AND cr2.state = com.e106.reco.domain.chat.entity.RoomState.PERSONAL")
    Optional<Room> findPersonalChatRoomBetweenArtists(@Param("artistSeq1") Long artistSeq1,
                                                      @Param("artistSeq2") Long artistSeq2);

}
