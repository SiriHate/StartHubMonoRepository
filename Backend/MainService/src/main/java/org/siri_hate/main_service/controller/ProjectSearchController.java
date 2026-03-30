package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ProjectSearchApi;
import org.siri_hate.main_service.dto.*;
import org.siri_hate.main_service.service.EmployeeSearchService;
import org.siri_hate.main_service.service.FounderSearchService;
import org.siri_hate.main_service.service.InvestorSearchService;
import org.siri_hate.main_service.service.MentorSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectSearchController implements ProjectSearchApi {

    private final EmployeeSearchService employeeSearchService;
    private final FounderSearchService founderSearchService;
    private final InvestorSearchService investorSearchService;
    private final MentorSearchService mentorSearchService;

    @Autowired
    public ProjectSearchController(
            EmployeeSearchService employeeSearchService,
            FounderSearchService founderSearchService,
            InvestorSearchService investorSearchService,
            MentorSearchService mentorSearchService
    ) {
        this.employeeSearchService = employeeSearchService;
        this.founderSearchService = founderSearchService;
        this.investorSearchService = investorSearchService;
        this.mentorSearchService = mentorSearchService;
    }

    @Override
    public ResponseEntity<EmployeeSearchFullResponseDTO> createEmployeeSearch(Long projectId, EmployeeSearchRequestDTO request) {
        var response = employeeSearchService.create(projectId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EmployeeSearchFullResponseDTO>> getEmployeeSearches(Long projectId) {
        var response = employeeSearchService.getByProjectId(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeSearchFullResponseDTO> updateEmployeeSearch(Long projectId, Long id, EmployeeSearchRequestDTO request) {
        var response = employeeSearchService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteEmployeeSearch(Long projectId, Long id) {
        employeeSearchService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<FounderSearchFullResponseDTO> createFounderSearch(Long projectId, FounderSearchRequestDTO request) {
        var response = founderSearchService.create(projectId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<FounderSearchFullResponseDTO>> getFounderSearches(Long projectId) {
        var response = founderSearchService.getByProjectId(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FounderSearchFullResponseDTO> updateFounderSearch(Long projectId, Long id, FounderSearchRequestDTO request) {
        var response = founderSearchService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteFounderSearch(Long projectId, Long id) {
        founderSearchService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<InvestorSearchFullResponseDTO> createInvestorSearch(Long projectId, InvestorSearchRequestDTO request) {
        var response = investorSearchService.create(projectId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<InvestorSearchFullResponseDTO>> getInvestorSearches(Long projectId) {
        var response = investorSearchService.getByProjectId(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvestorSearchFullResponseDTO> updateInvestorSearch(Long projectId, Long id, InvestorSearchRequestDTO request) {
        var response = investorSearchService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteInvestorSearch(Long projectId, Long id) {
        investorSearchService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<MentorSearchFullResponseDTO> createMentorSearch(Long projectId, MentorSearchRequestDTO request) {
        var response = mentorSearchService.create(projectId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<MentorSearchFullResponseDTO>> getMentorSearches(Long projectId) {
        var response = mentorSearchService.getByProjectId(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MentorSearchFullResponseDTO> updateMentorSearch(Long projectId, Long id, MentorSearchRequestDTO request) {
        var response = mentorSearchService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteMentorSearch(Long projectId, Long id) {
        mentorSearchService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
