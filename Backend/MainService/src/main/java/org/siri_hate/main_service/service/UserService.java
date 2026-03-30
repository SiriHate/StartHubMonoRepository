package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.main_service.kafka.producer.UserEventProducer;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserService(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    public User findOrCreateUser(String username) {
        return userRepository.findUserByUsername(username).orElseGet(() -> {
            var user = new User(username);
            return userRepository.save(user);
        });
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
        userEventProducer.sendUserDeletionMessage(username);
    }
}