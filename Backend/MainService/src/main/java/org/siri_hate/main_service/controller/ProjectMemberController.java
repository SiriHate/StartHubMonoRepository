package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.ProjectMemberApi;
import org.siri_hate.main_service.dto.ProjectMemberFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectMemberRequestDTO;
import org.siri_hate.main_service.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectMemberController implements ProjectMemberApi {

    private final ProjectMemberService memberService;

    @Autowired
    public ProjectMemberController(ProjectMemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public ResponseEntity<List<ProjectMemberFullResponseDTO>> getProjectMembers(Long projectId) {
        var response = memberService.getMembers(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectMemberFullResponseDTO> addProjectMember(Long projectId, ProjectMemberRequestDTO request) {
        var response = memberService.addMember(projectId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> removeProjectMember(Long projectId, Long memberId) {
        memberService.removeMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
