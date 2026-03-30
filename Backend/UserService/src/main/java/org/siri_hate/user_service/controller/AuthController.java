package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.AuthApi;
import org.siri_hate.user_service.dto.AccessTokenResponseDTO;
import org.siri_hate.user_service.dto.RefreshTokenRequestDTO;
import org.siri_hate.user_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<AccessTokenResponseDTO> refreshToken(RefreshTokenRequestDTO request) {
        var response = authService.refreshAccessToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}