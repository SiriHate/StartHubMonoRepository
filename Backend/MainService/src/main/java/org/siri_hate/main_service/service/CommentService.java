package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.main_service.dto.CommentRequestDTO;
import org.siri_hate.main_service.dto.CommentResponseDTO;
import org.siri_hate.main_service.model.entity.project.ProjectComment;
import org.siri_hate.main_service.model.mapper.CommentMapper;
import org.siri_hate.main_service.model.entity.project.Project;
import org.siri_hate.main_service.model.entity.User;
import org.siri_hate.main_service.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            ProjectService projectService,
            UserService userService,
            CommentMapper commentMapper
    ) {
        this.commentRepository = commentRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    @Transactional
    public CommentResponseDTO createComment(String username, Long projectId, CommentRequestDTO request) {
        Project project = projectService.getProjectEntity(projectId);
        User user = userService.findOrCreateUser(username);
        var comment = new ProjectComment(user, project, request.getText());
        commentRepository.save(comment);
        return commentMapper.toCommentResponseDTO(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        ProjectComment projectComment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
        String commentOwnerUsername = projectComment.getAuthor().getUsername();
        if (!commentOwnerUsername.equals(username)) {
            throw new RuntimeException();
        }
        commentRepository.delete(projectComment);
    }

    @Transactional
    public List<CommentResponseDTO> getProjectComments(Long projectId) {
        List<ProjectComment> projectComments = commentRepository.findByProjectId(projectId);
        return commentMapper.toCommentFullResponseListDTO(projectComments);
    }
}
