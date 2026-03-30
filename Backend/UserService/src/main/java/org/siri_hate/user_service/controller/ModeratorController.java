package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.ModeratorApi;
import org.siri_hate.user_service.dto.ModeratorFullResponseDTO;
import org.siri_hate.user_service.dto.ModeratorPageResponseDTO;
import org.siri_hate.user_service.dto.ModeratorRequestDTO;
import org.siri_hate.user_service.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModeratorController implements ModeratorApi {

    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @Override
    public ResponseEntity<ModeratorFullResponseDTO> createModerator(ModeratorRequestDTO moderatorRequestDTO) {
        var response = moderatorService.createModerator(moderatorRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteModerator(Long id) {
        moderatorService.deleteModerator(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ModeratorFullResponseDTO> getModerator(Long id) {
        var response = moderatorService.getModerator(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ModeratorPageResponseDTO> getModerators(Integer page, Integer size, String usernameQuery) {
        var response = moderatorService.getModeratorsPage(page, size, usernameQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ModeratorFullResponseDTO> updateModerator(Long id, ModeratorRequestDTO moderatorRequestDTO) {
        var response = moderatorService.updateModerator(id, moderatorRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}