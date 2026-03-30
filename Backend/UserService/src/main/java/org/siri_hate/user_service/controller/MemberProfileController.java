package org.siri_hate.user_service.controller;

import org.siri_hate.user_service.api.MemberProfileApi;
import org.siri_hate.user_service.dto.*;
import org.siri_hate.user_service.service.MemberProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberProfileController implements MemberProfileApi {

    private final MemberProfileService memberProfileService;

    @Autowired
    public MemberProfileController(MemberProfileService memberProfileService) {
        this.memberProfileService = memberProfileService;
    }

    @Override
    public ResponseEntity<List<DomainResponseDTO>> getAllDomains() {
        var response = memberProfileService.getAllDomains();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FounderProfileResponseDTO> getFounderProfile(Long memberId) {
        var response = memberProfileService.getFounderProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FounderProfileResponseDTO> createFounderProfile(Long memberId, FounderProfileRequestDTO request) {
        var response = memberProfileService.createFounderProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FounderProfileResponseDTO> updateFounderProfile(Long memberId, FounderProfileRequestDTO request) {
        var response = memberProfileService.updateFounderProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteFounderProfile(Long memberId) {
        memberProfileService.deleteFounderProfile(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<InvestorProfileResponseDTO> getInvestorProfile(Long memberId) {
        var response = memberProfileService.getInvestorProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvestorProfileResponseDTO> createInvestorProfile(Long memberId, InvestorProfileRequestDTO request) {
        var response = memberProfileService.createInvestorProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<InvestorProfileResponseDTO> updateInvestorProfile(Long memberId, InvestorProfileRequestDTO request) {
        var response = memberProfileService.updateInvestorProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteInvestorProfile(Long memberId) {
        memberProfileService.deleteInvestorProfile(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<MentorProfileResponseDTO> getMentorProfile(Long memberId) {
        var response = memberProfileService.getMentorProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<JobSeekerSpecializationResponseDTO>> getSpecializations() {
        var response = memberProfileService.getJobSeekerSpecializations();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MentorProfileResponseDTO> createMentorProfile(Long memberId, MentorProfileRequestDTO request) {
        var response = memberProfileService.createMentorProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MentorProfileResponseDTO> updateMentorProfile(Long memberId, MentorProfileRequestDTO request) {
        var response = memberProfileService.updateMentorProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteMentorProfile(Long memberId) {
        memberProfileService.deleteMentorProfile(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<JobSeekerProfileResponseDTO> getJobSeekerProfile(Long memberId) {
        var response = memberProfileService.getJobSeekerProfile(memberId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JobSeekerProfileResponseDTO> createJobSeekerProfile(Long memberId, JobSeekerProfileRequestDTO request) {
        var response = memberProfileService.createJobSeekerProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JobSeekerProfileResponseDTO> updateJobSeekerProfile(Long memberId, JobSeekerProfileRequestDTO request) {
        var response = memberProfileService.updateJobSeekerProfile(memberId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteJobSeekerProfile(Long memberId) {
        memberProfileService.deleteJobSeekerProfile(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
