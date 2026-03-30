package org.siri_hate.main_service.model.entity.project;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.User;

@Entity
@Table(
        name = "project_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_id"})
)
public class ProjectLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public ProjectLike() {}

    public ProjectLike(User user, Project project) {
        this.user = user;
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }
}
