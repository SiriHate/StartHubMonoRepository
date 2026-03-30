package org.siri_hate.user_service.service;

import jakarta.transaction.Transactional;
import org.siri_hate.user_service.dto.AccessTokenResponseDTO;
import org.siri_hate.user_service.dto.RefreshTokenRequestDTO;
import org.siri_hate.user_service.model.entity.RefreshToken;
import org.siri_hate.user_service.model.entity.User;
import org.siri_hate.user_service.repository.RefreshTokenRepository;
import org.siri_hate.user_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthService(
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService,
            UserDetailsService userDetailsService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public RefreshToken createRefreshToken(User user) {
        String token = jwtService.generateRefreshToken(user);
        Instant expiresAt = Instant.now().plusMillis(jwtService.getRefreshTokenExpiration());
        RefreshToken refreshToken = new RefreshToken(user, token, expiresAt);
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public AccessTokenResponseDTO refreshAccessToken(RefreshTokenRequestDTO request) {
        String refreshTokenValue = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(refreshTokenValue)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new BadCredentialsException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new BadCredentialsException("Refresh token has expired");
        }

        if (!jwtService.isTokenValid(refreshTokenValue)) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new BadCredentialsException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshTokenValue);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return new AccessTokenResponseDTO(newAccessToken);
    }
}
