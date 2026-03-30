package org.siri_hate.user_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.user_service.dto.ModeratorFullResponseDTO;
import org.siri_hate.user_service.dto.ModeratorPageResponseDTO;
import org.siri_hate.user_service.dto.ModeratorRequestDTO;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.mapper.ModeratorMapper;
import org.siri_hate.user_service.model.entity.Moderator;
import org.siri_hate.user_service.repository.ModeratorRepository;
import org.siri_hate.user_service.repository.adapters.ModeratorSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ModeratorService {

    private final ModeratorRepository moderatorRepository;
    private final ModeratorMapper moderatorMapper;
    private final UserEventProducer userEventProducer;

    @Autowired
    public ModeratorService(
            ModeratorRepository moderatorRepository,
            ModeratorMapper moderatorMapper,
            UserEventProducer userEventProducer
    )
    {
        this.moderatorRepository = moderatorRepository;
        this.moderatorMapper = moderatorMapper;
        this.userEventProducer = userEventProducer;
    }

    @Transactional
    public ModeratorFullResponseDTO createModerator(ModeratorRequestDTO request) {
        if (moderatorRepository.findModeratorByUsername(request.getUsername()).isPresent()) {
            throw new EntityExistsException();
        }
        Moderator moderator = moderatorMapper.toModerator(request);
        moderatorRepository.save(moderator);
        return moderatorMapper.toModeratorFullResponseDTO(moderator);
    }

    @Transactional
    public ModeratorPageResponseDTO getModeratorsPage(int page, int size, String usernameQuery) {
        Specification<Moderator> specification = Specification.allOf(
                ModeratorSpecification.usernameStartsWithIgnoreCase(usernameQuery)
        );
        Page<Moderator> moderators = moderatorRepository.findAll(specification, PageRequest.of(page, size));
        if (moderators.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return moderatorMapper.toModeratorPageResponseDTO(moderators);
    }

    @Transactional
    public ModeratorFullResponseDTO getModerator(Long id) {
        Moderator moderator = moderatorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return moderatorMapper.toModeratorFullResponseDTO(moderator);
    }

    @Transactional
    public ModeratorFullResponseDTO updateModerator(Long id, ModeratorRequestDTO request) {
        Moderator moderator = moderatorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        moderator = moderatorMapper.updateModerator(request, moderator);
        moderatorRepository.save(moderator);
        return moderatorMapper.toModeratorFullResponseDTO(moderator);
    }

    @Transactional
    public void deleteModerator(Long id) {
        Moderator moderator  = moderatorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        moderatorRepository.delete(moderator);
        userEventProducer.sendUserDeletionMessage(moderator.getUsername());
    }
}
