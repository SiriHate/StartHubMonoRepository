package org.siri_hate.user_service.model.entity.member_profiles;

import jakarta.persistence.*;
import org.siri_hate.user_service.model.entity.Member;

@Entity
@Table(name = "job_seeker_profiles")
public class JobSeekerProfile {

    @Id
    private Long memberId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String about;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private JobSeekerSpecialization specialization;

    public JobSeekerProfile() {}

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public JobSeekerSpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(JobSeekerSpecialization specialization) {
        this.specialization = specialization;
    }
}
