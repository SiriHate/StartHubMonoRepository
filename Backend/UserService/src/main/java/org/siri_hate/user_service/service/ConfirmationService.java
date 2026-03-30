package org.siri_hate.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.user_service.dto.RegistrationTokenRequestDTO;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.entity.ConfirmationToken;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.enums.ConfirmationTokenType;
import org.siri_hate.user_service.kafka.message.enums.ConfirmationMessageType;
import org.siri_hate.user_service.repository.ConfirmationTokenRepository;
import org.siri_hate.user_service.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserService userService;
    private final UserEventProducer userEventProducer;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public ConfirmationService(
            ConfirmationTokenRepository confirmationTokenRepository,
            UserService userService,
            UserEventProducer userEventProducer,
            TokenGenerator tokenGenerator
    )
    {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userService = userService;
        this.userEventProducer = userEventProducer;
        this.tokenGenerator = tokenGenerator;
    }

    public void sendRegistrationConfirmation(Member member) {
        String token = tokenGenerator.generateRandomToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                ConfirmationTokenType.CONFIRM_REGISTRATION,
                token,
                member
        );
        confirmationTokenRepository.save(confirmationToken);
        userEventProducer.sendConfirmationToken(
                member.getName(),
                member.getEmail(),
                token,
                ConfirmationMessageType.REGISTRATION_CONFIRMATION
        );
    }

    public void sendChangePasswordConfirmation(Member member) {
        String token = tokenGenerator.generateRandomToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                ConfirmationTokenType.CONFIRM_CHANGE_PASSWORD,
                token,
                member
        );
        confirmationTokenRepository.save(confirmationToken);
        userEventProducer.sendConfirmationToken(
                member.getName(),
                member.getEmail(),
                token,
                ConfirmationMessageType.CHANGE_PASSWORD_CONFIRMATION
        );
    }

    @Transactional
    public void checkConfirmationToken(RegistrationTokenRequestDTO request) {
        String token = request.getToken();
        checkConfirmationTokenByToken(token);
    }

    @Transactional
    public void checkConfirmationTokenByToken(String token) {
        ConfirmationToken tokenEntity = confirmationTokenRepository.findConfirmationTokenByTokenValue(token).orElseThrow(EntityNotFoundException::new);
        Long userId = tokenEntity.getMember().getId();
        userService.activateUserAccount(userId);
    }

    public Long getUserIdByToken(String token) {
        ConfirmationToken foundToken = confirmationTokenRepository.findConfirmationTokenByTokenValue(token).orElseThrow(EntityNotFoundException::new);
        return foundToken.getMember().getId();
    }

    public void deleteConfirmationTokenByTokenValue(String token) {
        ConfirmationToken foundToken = confirmationTokenRepository.findConfirmationTokenByTokenValue(token).orElseThrow(EntityNotFoundException::new);
        confirmationTokenRepository.delete(foundToken);
    }

    public void findConfirmationTokenByTokenValue(String token) {
        confirmationTokenRepository.findConfirmationTokenByTokenValue(token).orElseThrow(EntityNotFoundException::new);
    }
}