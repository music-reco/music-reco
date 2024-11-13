package com.e106.reco.domain.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "chat")
public class Chat {
    @Id
    private String seq;
    private String msg;
    private String artistSeq;
    private String roomSeq;

    private LocalDateTime createdAt;

}
