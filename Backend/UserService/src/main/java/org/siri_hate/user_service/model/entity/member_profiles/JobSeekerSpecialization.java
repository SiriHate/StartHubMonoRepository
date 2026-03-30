package org.siri_hate.user_service.model.entity.member_profiles;

import jakarta.persistence.*;

@Entity
@Table(name = "member_specializations")
public class JobSeekerSpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public JobSeekerSpecialization() {}

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