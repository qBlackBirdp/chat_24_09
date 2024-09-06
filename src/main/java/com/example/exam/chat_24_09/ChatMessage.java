package com.example.exam.chat_24_09;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ChatMessage {
    private long id;
    private LocalDateTime createDate;
    private String authorName;
    private String content;

    public ChatMessage(String authorName, String content) {
        this(ChatMessageIdGenerator.getNateId(), LocalDateTime.now(), authorName, content);
    }
    class ChatMessageIdGenerator {
        private static long id = 0;

        public static long getNateId() {
            return id++;
        }
    }
}