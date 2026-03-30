package org.siri_hate.user_service.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.user_service.client.YandexClient;
import org.siri_hate.user_service.dto.*;
import org.siri_hate.user_service.model.entity.member_profiles.MemberProfileType;
import org.siri_hate.user_service.model.mapper.MemberMapper;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.entity.RefreshToken;
import org.siri_hate.user_service.model.enums.AuthType;
import org.siri_hate.user_service.model.mapper.MemberProfileMapper;
import org.siri_hate.user_service.model.yandex.YandexUserInfo;
import org.siri_hate.user_service.repository.MemberRepository;
import org.siri_hate.user_service.repository.adapters.MemberSpecification;
import org.siri_hate.user_service.security.CredentialService;
import org.siri_hate.user_service.security.JwtService;
import org.siri_hate.user_service.kafka.producer.UserEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ConfirmationService confirmationService;
    private final UserEventProducer userEventProducer;
    private final MemberMapper memberMapper;
    private final FileService fileService;
    private final YandexClient yandexClient;
    private final JwtService jwtService;
    private final CredentialService credentialService;
    private final AuthService authService;
    private final MemberProfileMapper memberProfileMapper;

    @Autowired
    public MemberService(
            MemberRepository memberRepository,
            ConfirmationService confirmationService,
            UserEventProducer userEventProducer,
            MemberMapper memberMapper,
            FileService fileService,
            YandexClient yandexClient,
            JwtService jwtService,
            CredentialService credentialService,
            AuthService authService,
            MemberProfileMapper memberProfileMapper
    )
    {
        this.memberRepository = memberRepository;
        this.confirmationService = confirmationService;
        this.userEventProducer = userEventProducer;
        this.memberMapper = memberMapper;
        this.fileService = fileService;
        this.yandexClient = yandexClient;
        this.jwtService = jwtService;
        this.credentialService = credentialService;
        this.authService = authService;
        this.memberProfileMapper = memberProfileMapper;
    }

    @Transactional
    public void registerMember(MemberRegistrationRequestDTO request) {
        String username = request.getUsername();
        if (memberRepository.findMemberByUsername(username).isPresent()) {
            throw new EntityExistsException();
        }
        Member member = memberMapper.toMember(request);
        memberRepository.save(member);
        userEventProducer.sendSuccessfulRegistrationNotification(member);
        confirmationService.sendRegistrationConfirmation(member);
    }

    @Transactional
    public SuccessfulLoginResponseDTO loginViaYandex(YandexLoginRequestDTO request) {
        YandexUserInfo yandexUserInfo = yandexClient.fetchYandexUserInfo(request.getToken());
        if (yandexUserInfo == null) {
            throw new UsernameNotFoundException("Yandex user not found");
        }
        Member member = memberRepository.findMemberByEmail(yandexUserInfo.getDefaultEmail()).orElseGet(() -> {
            Member newMember = memberMapper.toMember(yandexUserInfo);
            newMember.setAuthType(AuthType.YANDEX);
            memberRepository.save(newMember);
            confirmationService.sendRegistrationConfirmation(newMember);
            userEventProducer.sendSuccessfulRegistrationNotification(newMember);
            return newMember;
        });
        String username = member.getUsername();
        String accessToken = jwtService.generateAccessToken(member);
        RefreshToken refreshToken = authService.createRefreshToken(member);
        return new SuccessfulLoginResponseDTO(username, accessToken, refreshToken.getToken(), UserRoleDTO.MEMBER);
    }

    public void requestPasswordRecovery(RecoveryPasswordRequestDTO recoveryPasswordRequest) {
        String email = recoveryPasswordRequest.getEmail();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(EntityNotFoundException::new);
        confirmationService.sendChangePasswordConfirmation(member);
    }

    @Transactional
    public void confirmPasswordRecovery(ChangePasswordTokenRequestDTO changePasswordTokenRequest) {
        String token = changePasswordTokenRequest.getToken();
        Long userId = confirmationService.getUserIdByToken(token);
        Member member = memberRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        String newPassword = changePasswordTokenRequest.getNewPassword();
        credentialService.setPassword(member, newPassword);
        memberRepository.save(member);
        confirmationService.deleteConfirmationTokenByTokenValue(token);
        userEventProducer.sendChangedPasswordNotification(member.getName(), member.getEmail());
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequestDTO changePasswordForm) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        credentialService.changePassword(
                changePasswordForm.getCurrentPassword(),
                changePasswordForm.getNewPassword(),
                member
        );
        memberRepository.save(member);
    }

    public MemberPageResponseDTO getMembers(
            String query,
            ProfileTypeDTO profileTypeDTO,
            String domain,
            int page,
            int size
    )
    {
        Specification<Member> spec = Specification.where(MemberSpecification.usernameContainsIgnoreCase(query));
        if (profileTypeDTO != null) {
            MemberProfileType profileType = memberProfileMapper.toMemberProfileType(profileTypeDTO);
            switch (profileType) {
                case JOB_SEEKER -> spec = spec.and(MemberSpecification.hasJobSeekerProfile()).and(MemberSpecification.jobSeekerHasSpecialization(domain));
                case MENTOR -> spec = spec.and(MemberSpecification.hasMentorProfile()).and(MemberSpecification.mentorHasDomain(domain));
                case INVESTOR -> spec = spec.and(MemberSpecification.hasInvestorProfile()).and(MemberSpecification.investorHasDomain(domain));
                case FOUNDER -> spec = spec.and(MemberSpecification.hasFounderProfile()).and(MemberSpecification.founderHasDomain(domain));
            }
        }
        Page<Member> members = memberRepository.findAll(spec, PageRequest.of(page, size));
        return memberMapper.toMemberPageResponseDTO(members);
    }

    public MemberFullResponseDTO getMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return memberMapper.toMemberFullResponseDTO(member);
    }

    public MemberFullResponseDTO getMember(String username) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        return memberMapper.toMemberFullResponseDTO(member);
    }

    @Transactional
    public MemberFullResponseDTO updateMember(String username, MemberProfileDataRequestDTO request) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        member = memberMapper.updateMember(request, member);
        memberRepository.save(member);
        return memberMapper.toMemberFullResponseDTO(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member domain = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        memberRepository.delete(domain);
        userEventProducer.sendUserDeletionMessage(domain.getUsername());
    }

    @Transactional
    public void deleteMember(String username) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        memberRepository.delete(member);
        userEventProducer.sendDeletedAccountNotification(member.getName(), member.getEmail());
        userEventProducer.sendUserDeletionMessage(member.getUsername());
    }

    @Transactional
    public void changeMemberAvatar(String username, MultipartFile file) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        String previousAvatarKey = member.getAvatarKey();
        fileService.deleteAvatar(previousAvatarKey);
        String newAvatarKey = fileService.uploadAvatar(username, file);
        member.setAvatarKey(newAvatarKey);
        memberRepository.save(member);
    }

    @Transactional
    public MemberFullResponseDTO changePersonalInfo(String username, MemberProfileDataRequestDTO request) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        member = memberMapper.updateMember(request, member);
        memberRepository.save(member);
        return memberMapper.toMemberFullResponseDTO(member);
    }
}