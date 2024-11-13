package com.e106.reco.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long roomSeq;
    private List<ChatRoomResponse> chatRoomResponses;
    private String lastMsg;
    private LocalDateTime lastMsgTime;
}
