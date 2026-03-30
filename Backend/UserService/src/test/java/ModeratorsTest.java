import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siri_hate.user_service.dto.ModeratorFullResponseDTO;
import org.siri_hate.user_service.dto.ModeratorRequestDTO;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.entity.Moderator;
import org.siri_hate.user_service.model.mapper.ModeratorMapper;
import org.siri_hate.user_service.repository.ModeratorRepository;
import org.siri_hate.user_service.service.ModeratorService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModeratorsTest {

    @Mock
    private ModeratorRepository moderatorRepository;
    @Mock
    private ModeratorMapper moderatorMapper;
    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private ModeratorService moderatorService;

    @Test
    void saveAndReturnResponse() {
        ModeratorRequestDTO request = new ModeratorRequestDTO();
        request.setUsername("moderator");

        Moderator moderator = new Moderator();
        moderator.setUsername("moderator");

        ModeratorFullResponseDTO response = new ModeratorFullResponseDTO();

        when(moderatorRepository.findModeratorByUsername("moderator")).thenReturn(Optional.empty());
        when(moderatorMapper.toModerator(request)).thenReturn(moderator);
        when(moderatorMapper.toModeratorFullResponseDTO(moderator)).thenReturn(response);

        ModeratorFullResponseDTO actual = moderatorService.createModerator(request);

        assertSame(response, actual);
        verify(moderatorRepository).save(moderator);
    }

    @Test
    void throwWhenUsernameExists() {
        ModeratorRequestDTO request = new ModeratorRequestDTO();
        request.setUsername("moderator");

        when(moderatorRepository.findModeratorByUsername("moderator")).thenReturn(Optional.of(new Moderator()));

        assertThrows(EntityExistsException.class, () -> moderatorService.createModerator(request));
        verify(moderatorRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
