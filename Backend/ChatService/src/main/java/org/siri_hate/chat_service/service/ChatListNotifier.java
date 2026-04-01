package org.siri_hate.chat_service.service;

import org.siri_hate.chat_service.repository.ChatMemberRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChatListNotifier {

    private static final String USER_CHAT_LIST_DESTINATION = "/queue/chat-list";

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMemberRepository chatMemberRepository;

    public ChatListNotifier(
            SimpMessagingTemplate messagingTemplate,
            ChatMemberRepository chatMemberRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.chatMemberRepository = chatMemberRepository;
    }

    public void notifyAllMembersOfChat(Long chatId) {
        if (chatId == null) return;
        for (String username : chatMemberRepository.findUsernamesByChatId(chatId)) {
            notifyUser(username);
        }
    }

    public void notifyUser(String username) {
        if (username == null || username.isBlank()) return;
        messagingTemplate.convertAndSendToUser(
                username,
                USER_CHAT_LIST_DESTINATION,
                Map.of("type", "CHAT_LIST_CHANGED")
        );
    }
}