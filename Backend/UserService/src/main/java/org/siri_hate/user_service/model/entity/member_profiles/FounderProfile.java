package org.siri_hate.user_service.model.entity.member_profiles;

import jakarta.persistence.*;
import org.siri_hate.user_service.model.entity.Member;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "founder_profiles")
public class FounderProfile {

    @Id
    private Long memberId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String about;

    @ManyToMany
    @JoinTable(
            name = "founder_domains",
            joinColumns = @JoinColumn(name = "founder_id"),
            inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> domains = new HashSet<>();

    public FounderProfile() {}

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

    public Set<Domain> getDomains() {
        return domains;
    }

    public void setDomains(Set<Domain> domains) {
        this.domains = domains;
    }
}
