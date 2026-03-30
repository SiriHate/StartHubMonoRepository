package org.siri_hate.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.user_service.dto.*;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.entity.RefreshToken;
import org.siri_hate.user_service.model.mapper.UserMapper;
import org.siri_hate.user_service.model.entity.User;
import org.siri_hate.user_service.repository.UserRepository;
import org.siri_hate.user_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;
    private final AuthService authService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserMapper userMapper,
            UserEventProducer userEventProducer,
            AuthService authService
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.userEventProducer = userEventProducer;
        this.authService = authService;
    }

    public SuccessfulLoginResponseDTO login(SimpleLoginRequestDTO simpleLoginRequest) {
        String username = simpleLoginRequest.getUsername();
        String password = simpleLoginRequest.getPassword();
        var authToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authToken);
        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Username or password is invalid");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (Objects.isNull(userDetails)) {
            throw new RuntimeException();
        }
        User user = (User) userDetails;
        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken refreshToken = authService.createRefreshToken(user);
        String role = userDetails.getAuthorities().iterator().next().toString();
        return new SuccessfulLoginResponseDTO(username, accessToken, refreshToken.getToken(), UserRoleDTO.valueOf(role));
    }

    public User getUser(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public CurrentUserInfoResponseDTO getCurrentUser(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(EntityNotFoundException::new);
        return userMapper.userToCurrentUserInfoResponseDTO(user);
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
    }

    public void activateUserAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        user.setEnabled(true);
        userRepository.save(user);
        userEventProducer.sendSuccessfulRegistrationNotification(user);
    }
}