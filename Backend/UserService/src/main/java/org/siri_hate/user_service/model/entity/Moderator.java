package org.siri_hate.user_service.model.entity;

import jakarta.persistence.*;
import org.jspecify.annotations.NullMarked;
import org.siri_hate.user_service.model.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "moderators")
@PrimaryKeyJoinColumn(name = "user_id")
public class Moderator extends User {

    @Column(nullable = false)
    private String name;

    @Column(name = "employee_id", nullable = false, unique = true)
    private Long employeeId;

    public Moderator() {
        this.role = UserRole.MODERATOR;
        this.isEnabled = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
