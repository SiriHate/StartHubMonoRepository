package org.siri_hate.main_service.model.entity.project;

import jakarta.persistence.*;

@Entity
@Table(
        name = "project_categories",
        indexes = @Index(name = "category_name_idx",  columnList="name", unique = true)
)
public class ProjectCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public ProjectCategory() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
