package org.siri_hate.main_service.model.entity.project.search;

import jakarta.persistence.*;
import org.siri_hate.main_service.model.entity.project.Project;

@Entity
@Table(name = "project_investor_search")
public class InvestorSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String about;

    public InvestorSearch() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
