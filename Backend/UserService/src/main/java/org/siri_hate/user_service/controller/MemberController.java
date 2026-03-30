package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.MemberApi;
import org.siri_hate.user_service.dto.*;
import org.siri_hate.user_service.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public ResponseEntity<Void> changeCurrentMemberAvatar(String xUserName, MultipartFile file) {
        memberService.changeMemberAvatar(xUserName, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> changeCurrentMemberPassword(String xUserName, ChangePasswordRequestDTO changePasswordRequestDTO) {
        memberService.changePassword(xUserName, changePasswordRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteCurrentMember(String xUserName) {
        memberService.deleteMember(xUserName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteMemberById(Long id) {
        memberService.deleteMember(id);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteMemberByUsername(String username) {
        memberService.deleteMember(username);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<MemberFullResponseDTO> getCurrentMember(String xUserName) {
        var response = memberService.getMember(xUserName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MemberFullResponseDTO> getMemberById(Long id) {
        var response = memberService.getMember(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MemberFullResponseDTO> getMemberByUsername(String username) {
        var response = memberService.getMember(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MemberPageResponseDTO> getMembers(
            Integer page,
            Integer size,
            String query,
            ProfileTypeDTO profileType,
            String domain
    ) {
        var response = memberService.getMembers(query, profileType, domain, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> memberPasswordRecoveryConfirm(String token, ChangePasswordTokenRequestDTO changePasswordTokenRequestDTO) {
        changePasswordTokenRequestDTO.setToken(token);
        memberService.confirmPasswordRecovery(changePasswordTokenRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> memberPasswordRecoveryRequest(RecoveryPasswordRequestDTO recoveryPasswordRequestDTO) {
        memberService.requestPasswordRecovery(recoveryPasswordRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> registerMember(MemberRegistrationRequestDTO memberRegistrationRequestDTO) {
        memberService.registerMember(memberRegistrationRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MemberFullResponseDTO> updateCurrentMember(String xUserName, MemberProfileDataRequestDTO memberProfileDataRequestDTO) {
        var response = memberService.updateMember(xUserName, memberProfileDataRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MemberFullResponseDTO> updateCurrentMemberProfile(String xUserName, MemberProfileDataRequestDTO memberProfileDataRequestDTO) {
        var response = memberService.changePersonalInfo(xUserName, memberProfileDataRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SuccessfulLoginResponseDTO> yandexLogin(YandexLoginRequestDTO yandexLoginRequestDTO) {
        var response = memberService.loginViaYandex(yandexLoginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}