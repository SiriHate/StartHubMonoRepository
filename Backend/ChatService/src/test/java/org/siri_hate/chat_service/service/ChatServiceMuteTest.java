package org.siri_hate.chat_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siri_hate.chat_service.model.entity.ChatMember;
import org.siri_hate.chat_service.model.mapper.ChatMapper;
import org.siri_hate.chat_service.model.mapper.ChatMemberMapper;
import org.siri_hate.chat_service.repository.ChatMemberRepository;
import org.siri_hate.chat_service.repository.ChatRepository;
import org.siri_hate.chat_service.repository.MessageRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceMuteTest {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatMapper chatMapper;
    @Mock
    private UserService userService;
    @Mock
    private ChatMemberMapper chatMemberMapper;
    @Mock
    private ChatMemberRepository chatMemberRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    void muteAndUnmuteChat_shouldChangeFlagAndPersist() {
        ChatMember chatMember = new ChatMember();
        when(chatMemberRepository.findByChatIdAndUserUsername(5L, "user")).thenReturn(Optional.of(chatMember));

        chatService.muteChat(5L, "user");
        assertTrue(chatMember.isMutedNotifications());
        verify(chatMemberRepository).save(chatMember);

        chatService.unmuteChat(5L, "user");
        assertFalse(chatMember.isMutedNotifications());
        verify(chatMemberRepository, times(2)).save(chatMember);
    }
}
