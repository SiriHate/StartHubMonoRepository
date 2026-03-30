package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.AdminApi;
import org.siri_hate.user_service.dto.AdminFullResponseDTO;
import org.siri_hate.user_service.dto.AdminPageResponseDTO;
import org.siri_hate.user_service.dto.AdminRequestDTO;
import org.siri_hate.user_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public ResponseEntity<AdminFullResponseDTO> createAdmin(AdminRequestDTO adminRequestDTO) {
        var response = adminService.createAdmin(adminRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteAdmin(Long id) {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<AdminFullResponseDTO> getAdmin(Long id) {
        var response = adminService.getAdmin(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AdminPageResponseDTO> getAdmins(Integer page, Integer size) {
        var response = adminService.getAdmins(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AdminFullResponseDTO> updateAdmin(Long id, AdminRequestDTO adminRequestDTO) {
        var response = adminService.updateAdmin(id, adminRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
