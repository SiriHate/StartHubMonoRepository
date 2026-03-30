package org.siri_hate.chat_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siri_hate.chat_service.dto.MessageRequestDTO;
import org.siri_hate.chat_service.dto.MessageResponseDTO;
import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.entity.Message;
import org.siri_hate.chat_service.model.entity.User;
import org.siri_hate.chat_service.model.mapper.MessageMapper;
import org.siri_hate.chat_service.repository.ChatMemberRepository;
import org.siri_hate.chat_service.repository.MessageRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private UserService userService;
    @Mock
    private ChatService chatService;
    @Mock
    private FileService fileService;
    @Mock
    private ChatMemberRepository chatMemberRepository;
    @Mock
    private ObjectProvider<KafkaTemplate<String, String>> kafkaTemplateProvider;
    @Mock
    private ObjectProvider<com.fasterxml.jackson.databind.ObjectMapper> objectMapperProvider;

    @InjectMocks
    private MessageService messageService;

    @Test
    void createMessage_shouldThrowWhenTextAndImageAreEmpty() {
        MessageRequestDTO request = new MessageRequestDTO();
        request.setChatId(10L);
        request.setContent("   ");
        request.setImageKey(null);

        when(messageMapper.toMessage(request)).thenReturn(new Message());

        assertThrows(IllegalArgumentException.class, () -> messageService.createMessage("user", request));
        verify(messageRepository, never()).save(any());
    }

    @Test
    void createMessage_shouldAllowImageOnlyAndNormalizeContent() {
        MessageRequestDTO request = new MessageRequestDTO();
        request.setChatId(10L);
        request.setContent("   ");
        request.setImageKey("  img-key  ");

        Message message = new Message();
        when(messageMapper.toMessage(request)).thenReturn(message);

        User sender = new User();
        Chat chat = new Chat();
        chat.setId(10L);
        when(userService.getOrCreateUser("user")).thenReturn(sender);
        when(chatService.getChatEntity(10L)).thenReturn(chat);
        when(chatMemberRepository.findRecipientUsernames(10L, "user")).thenReturn(List.of());
        when(messageMapper.toMessageResponse(message)).thenReturn(new MessageResponseDTO());

        messageService.createMessage("user", request);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());
        Message saved = captor.getValue();
        assertEquals("", saved.getContent());
        assertEquals("img-key", saved.getImageKey());
    }
}
