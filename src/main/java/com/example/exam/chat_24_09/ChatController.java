package com.example.exam.chat_24_09;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SseEmitters sseEmitters;

    private List<ChatMessage> chatMessages = new ArrayList<>();

//    @AllArgsConstructor
//    @Getter
//    public static class writeMessageRequest () {
//        private final String authorName;
//        private final String content;
//
//    }

    public record writeMessageRequest (String authorName, String content) {}
    public record writeMessageResponse (long id) {}
    public record messagesRequest (Long fromId) {}
    public record messagesResponse (List<ChatMessage> chatMessages, long count) {}

    @GetMapping("/room")
    public String showRoom() {
        return "chat/room";
    }

    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<writeMessageResponse> writeMessage(@RequestBody writeMessageRequest req) {

        ChatMessage message = new ChatMessage(req.authorName, req.content);

        chatMessages.add(message);

        sseEmitters.noti("chat__messageAdded");

        return new RsData<>("S-1", "메세지가 작성됨", new writeMessageResponse(message.getId()) );
    }

    @GetMapping("/messages")
    @ResponseBody
    public RsData<messagesResponse> messages(messagesRequest req) {
        List<ChatMessage> messages = chatMessages;
        log.debug("req: {}", req);

        //번호가 같이 입력되었다면?
        if (req.fromId != null) {
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId()==req.fromId)
                    .findFirst()
                    .orElse(-1);

            if (index != -1) {
                messages = messages.subList(index + 1, messages.size());
            }
        }

        return new RsData<>("S-1",
                "성공",
                new messagesResponse(messages, messages.size())
        );
    }
}