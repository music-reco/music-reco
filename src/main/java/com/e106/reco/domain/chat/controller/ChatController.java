package com.e106.reco.domain.chat.controller;

import com.e106.reco.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatRepository chatRepository;

//    @GetMapping(value = "/sender/{sender}/receiver/{receiver", produces = PageAttributes.MediaType.TEXT_EVENT_STREAM_VALUE)
//    public void getMsg(@PathVariable("sender") String sender, @PathVariable("receiver") String receiver) {
//
//    }

}
