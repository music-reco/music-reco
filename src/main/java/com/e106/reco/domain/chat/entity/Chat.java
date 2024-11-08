package com.e106.reco.domain.chat.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat")
public class Chat {
    @Id
    private String seq;
    private String msg;
    private String sender;
    private String receiver;

    private LocalDateTime createdAt;
}
