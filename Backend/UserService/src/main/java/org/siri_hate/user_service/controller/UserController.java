package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.UserApi;
import org.siri_hate.user_service.dto.CurrentUserInfoResponseDTO;
import org.siri_hate.user_service.dto.SimpleLoginRequestDTO;
import org.siri_hate.user_service.dto.SuccessfulLoginResponseDTO;
import org.siri_hate.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<CurrentUserInfoResponseDTO> getCurrentUser(String xUserName) {
        var response = userService.getCurrentUser(xUserName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SuccessfulLoginResponseDTO> simpleLogin(SimpleLoginRequestDTO simpleLoginRequestDTO) {
        var response = userService.login(simpleLoginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}