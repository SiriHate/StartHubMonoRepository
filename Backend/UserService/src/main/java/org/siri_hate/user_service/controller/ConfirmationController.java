package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.ConfirmationApi;
import org.siri_hate.user_service.service.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfirmationController implements ConfirmationApi {

    private final ConfirmationService confirmationService;

    @Autowired
    public ConfirmationController(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @Override
    public ResponseEntity<Void> checkConfirmationToken(String token) {
        confirmationService.findConfirmationTokenByTokenValue(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> confirmRegistration(String token) {
        confirmationService.checkConfirmationTokenByToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}