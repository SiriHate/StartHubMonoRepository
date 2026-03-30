package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.ProjectMemberFullResponseDTO;
import org.siri_hate.main_service.dto.ProjectMemberRequestDTO;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.project.ProjectMember;
import org.siri_hate.main_service.model.mapper.ProjectMemberMapper;
import org.siri_hate.main_service.repository.ProjectMemberRepository;
import org.siri_hate.main_service.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ProjectMemberMapper memberMapper;

    @Autowired
    public ProjectMemberService(
            ProjectMemberRepository memberRepository,
            ProjectRepository projectRepository,
            UserService userService,
            ProjectMemberMapper memberMapper
    ) {
        this.memberRepository = memberRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.memberMapper = memberMapper;
    }

    public List<ProjectMemberFullResponseDTO> getMembers(Long projectId) {
        List<ProjectMember> members = memberRepository.findByProjectId(projectId);
        return memberMapper.toProjectMembersFullResponseDTOList(members);
    }

    @Transactional
    public ProjectMemberFullResponseDTO addMember(Long projectId, ProjectMemberRequestDTO request) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(userService.findOrCreateUser(request.getUsername()));
        member.setRole(request.getRole());
        memberRepository.save(member);
        return memberMapper.toProjectMembersFullResponseDTO(member);
    }

    @Transactional
    public void removeMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
