package org.siri_hate.user_service.model.entity;

import jakarta.persistence.*;
import org.jspecify.annotations.NullMarked;
import org.siri_hate.user_service.model.entity.member_profiles.*;
import org.siri_hate.user_service.model.enums.AuthType;
import org.siri_hate.user_service.model.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "members")
@PrimaryKeyJoinColumn(name = "user_id")
public class Member extends User {

    @Column(name = "avatar_key")
    private String avatarKey;

    @Column(nullable = false)
    private String name;

    private String country;

    private String city;

    private String about;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private InvestorProfile investorProfile;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MentorProfile mentorProfile;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private JobSeekerProfile jobSeekerProfile;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private FounderProfile founderProfile;

    public Member() {
        this.role = UserRole.MEMBER;
    }

    public InvestorProfile getInvestorProfile() {
        return investorProfile;
    }

    public void setInvestorProfile(InvestorProfile investorProfile) {
        this.investorProfile = investorProfile;
    }

    public MentorProfile getMentorProfile() {
        return mentorProfile;
    }

    public void setMentorProfile(MentorProfile mentorProfile) {
        this.mentorProfile = mentorProfile;
    }

    public JobSeekerProfile getJobSeekerProfile() {
        return jobSeekerProfile;
    }

    public void setJobSeekerProfile(JobSeekerProfile jobSeekerProfile) {
        this.jobSeekerProfile = jobSeekerProfile;
    }

    public FounderProfile getFounderProfile() {
        return founderProfile;
    }

    public void setFounderProfile(FounderProfile founderProfile) {
        this.founderProfile = founderProfile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatarKey() {
        return avatarKey;
    }

    public void setAvatarKey(String avatarKey) {
        this.avatarKey = avatarKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }


    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
