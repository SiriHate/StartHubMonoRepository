import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siri_hate.user_service.client.YandexClient;
import org.siri_hate.user_service.dto.MemberRegistrationRequestDTO;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.mapper.MemberMapper;
import org.siri_hate.user_service.model.mapper.MemberProfileMapper;
import org.siri_hate.user_service.repository.MemberRepository;
import org.siri_hate.user_service.security.CredentialService;
import org.siri_hate.user_service.security.JwtService;
import org.siri_hate.user_service.service.AuthService;
import org.siri_hate.user_service.service.ConfirmationService;
import org.siri_hate.user_service.service.FileService;
import org.siri_hate.user_service.service.MemberService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembersTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ConfirmationService confirmationService;
    @Mock
    private UserEventProducer userEventProducer;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private FileService fileService;
    @Mock
    private YandexClient yandexClient;
    @Mock
    private JwtService jwtService;
    @Mock
    private CredentialService credentialService;
    @Mock
    private AuthService authService;
    @Mock
    private MemberProfileMapper memberProfileMapper;

    @InjectMocks
    private MemberService memberService;

    @Test
    void saveAndSendEvents() {
        MemberRegistrationRequestDTO request = new MemberRegistrationRequestDTO();
        request.setName("name");
        request.setEmail("email@example.com");
        request.setPhone("+79990001122");
        request.setBirthday(LocalDate.of(2000, 1, 1));
        request.setUsername("username");
        request.setPassword("password123");

        Member member = new Member();
        member.setUsername("username");
        member.setEmail("email@example.com");
        member.setName("name");

        when(memberRepository.findMemberByUsername("username")).thenReturn(Optional.empty());
        when(memberMapper.toMember(request)).thenReturn(member);

        memberService.registerMember(request);

        verify(memberRepository, times(1)).save(member);
        verify(userEventProducer, times(1)).sendSuccessfulRegistrationNotification(member);
        verify(confirmationService, times(1)).sendRegistrationConfirmation(member);
    }

    @Test
    void throwWhenUsernameExists() {
        MemberRegistrationRequestDTO request = new MemberRegistrationRequestDTO();
        request.setUsername("username");

        when(memberRepository.findMemberByUsername("username")).thenReturn(Optional.of(new Member()));

        assertThrows(EntityExistsException.class, () -> memberService.registerMember(request));

        verify(memberRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(userEventProducer, never()).sendSuccessfulRegistrationNotification(org.mockito.ArgumentMatchers.any());
        verify(confirmationService, never()).sendRegistrationConfirmation(org.mockito.ArgumentMatchers.any());
    }
}
