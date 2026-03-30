package org.siri_hate.user_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.user_service.dto.AdminFullResponseDTO;
import org.siri_hate.user_service.dto.AdminPageResponseDTO;
import org.siri_hate.user_service.dto.AdminRequestDTO;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.mapper.AdminMapper;
import org.siri_hate.user_service.model.entity.Admin;
import org.siri_hate.user_service.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    final private AdminRepository adminRepository;
    final private AdminMapper adminMapper;
    final private UserEventProducer userEventProducer;

    @Autowired
    public AdminService(
            AdminRepository adminRepository,
            AdminMapper adminMapper,
            UserEventProducer userEventProducer
    )
    {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.userEventProducer = userEventProducer;
    }

    @Transactional
    public AdminFullResponseDTO createAdmin(AdminRequestDTO request) {
        String username = request.getUsername();
        if (adminRepository.findAdminByUsername(username).isPresent()) {
            throw new EntityExistsException();
        }
        Admin admin = adminMapper.toAdmin(request);
        adminRepository.save(admin);
        return adminMapper.toAdminFullResponseDTO(admin);
    }

    public AdminPageResponseDTO getAdmins(Integer page, Integer size) {
        Page<Admin> admins = adminRepository.findAll(PageRequest.of(page, size));
        if (admins.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return adminMapper.toAdminPageResponseDTO(admins);
    }

    public AdminFullResponseDTO getAdmin(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return adminMapper.toAdminFullResponseDTO(admin);
    }

    @Transactional
    public AdminFullResponseDTO updateAdmin(Long id, AdminRequestDTO request) {
        Admin domain = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        domain = adminMapper.adminUpdate(request, domain);
        adminRepository.save(domain);
        return adminMapper.toAdminFullResponseDTO(domain);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        Admin domain = adminRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        adminRepository.delete(domain);
        userEventProducer.sendUserDeletionMessage(domain.getUsername());
    }
}