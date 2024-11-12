package com.e106.reco.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {
    private List<Long> receiversSeq;
    private Long senderSeq;
}
