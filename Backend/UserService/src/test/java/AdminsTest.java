import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siri_hate.user_service.dto.AdminFullResponseDTO;
import org.siri_hate.user_service.dto.AdminRequestDTO;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.entity.Admin;
import org.siri_hate.user_service.model.mapper.AdminMapper;
import org.siri_hate.user_service.repository.AdminRepository;
import org.siri_hate.user_service.service.AdminService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminsTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private AdminMapper adminMapper;
    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private AdminService adminService;

    @Test
    void saveAndReturnResponse() {
        AdminRequestDTO request = new AdminRequestDTO();
        request.setUsername("admin");

        Admin admin = new Admin();
        admin.setUsername("admin");

        AdminFullResponseDTO response = new AdminFullResponseDTO();

        when(adminRepository.findAdminByUsername("admin")).thenReturn(Optional.empty());
        when(adminMapper.toAdmin(request)).thenReturn(admin);
        when(adminMapper.toAdminFullResponseDTO(admin)).thenReturn(response);

        AdminFullResponseDTO actual = adminService.createAdmin(request);

        assertSame(response, actual);
        verify(adminRepository).save(admin);
    }

    @Test
    void throwWhenUsernameExists() {
        AdminRequestDTO request = new AdminRequestDTO();
        request.setUsername("admin");

        when(adminRepository.findAdminByUsername("admin")).thenReturn(Optional.of(new Admin()));

        assertThrows(EntityExistsException.class, () -> adminService.createAdmin(request));
        verify(adminRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
