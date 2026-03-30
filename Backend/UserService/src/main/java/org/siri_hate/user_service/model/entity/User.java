package org.siri_hate.user_service.model.entity;

import jakarta.persistence.*;
import org.jspecify.annotations.NullMarked;
import org.siri_hate.user_service.model.enums.UserRole;
import org.siri_hate.user_service.security.Credentialed;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "username_idx",  columnList="username", unique = true)
)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements UserDetails, Credentialed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true)
    protected String username;

    protected String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected UserRole role;

    @Column(name = "account_enabled", nullable = false)
    protected boolean isEnabled;

    public User() {
        this.isEnabled = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @NullMarked
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return isEnabled == user.isEnabled
                && Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(password, user.password)
                && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, isEnabled);
    }
}
