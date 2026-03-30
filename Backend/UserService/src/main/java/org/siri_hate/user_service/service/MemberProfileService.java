package org.siri_hate.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.user_service.dto.*;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.model.entity.member_profiles.*;
import org.siri_hate.user_service.model.mapper.*;
import org.siri_hate.user_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MemberProfileService {

    private final MemberRepository memberRepository;
    private final FounderProfileRepository founderProfileRepository;
    private final InvestorProfileRepository investorProfileRepository;
    private final MentorProfileRepository mentorProfileRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final DomainRepository domainRepository;
    private final JobSeekerSpecializationRepository jobSeekerSpecializationRepository;
    private final FounderProfileMapper founderProfileMapper;
    private final JobSeekerProfileMapper jobSeekerProfileMapper;
    private final InvestorProfileMapper investorProfileMapper;
    private final MentorProfileSeeker mentorProfileMapper;
    final private JobSeekerSpecializationMapper jobSeekerSpecializationMapper;

    @Autowired
    public MemberProfileService(
            MemberRepository memberRepository,
            FounderProfileRepository founderProfileRepository,
            InvestorProfileRepository investorProfileRepository,
            MentorProfileRepository mentorProfileRepository,
            JobSeekerProfileRepository jobSeekerProfileRepository,
            DomainRepository domainRepository,
            JobSeekerSpecializationRepository jobSeekerSpecializationRepository,
            FounderProfileMapper founderProfileMapper,
            JobSeekerProfileMapper jobSeekerProfileMapper,
            InvestorProfileMapper investorProfileMapper,
            MentorProfileSeeker mentorProfileMapper,
            JobSeekerSpecializationMapper jobSeekerSpecializationMapper
    ) {
        this.memberRepository = memberRepository;
        this.founderProfileRepository = founderProfileRepository;
        this.investorProfileRepository = investorProfileRepository;
        this.mentorProfileRepository = mentorProfileRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.domainRepository = domainRepository;
        this.jobSeekerSpecializationRepository = jobSeekerSpecializationRepository;
        this.founderProfileMapper = founderProfileMapper;
        this.jobSeekerProfileMapper = jobSeekerProfileMapper;
        this.investorProfileMapper = investorProfileMapper;
        this.mentorProfileMapper = mentorProfileMapper;
        this.jobSeekerSpecializationMapper = jobSeekerSpecializationMapper;
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
    }

    private Set<Domain> resolveDomains(List<Long> domainIds) {
        if (domainIds == null || domainIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(domainRepository.findAllById(domainIds));
    }

    private JobSeekerSpecialization resolveSpecialization(Long specializationId) {
        if (specializationId == null) return null;
        return jobSeekerSpecializationRepository.findById(specializationId).orElseThrow(EntityNotFoundException::new);
    }

    public List<DomainResponseDTO> getAllDomains() {
        return domainRepository.findAll().stream()
                .map(domain -> {
                    DomainResponseDTO dto = new DomainResponseDTO();
                    dto.setId(domain.getId());
                    dto.setName(domain.getName());
                    return dto;
                })
                .toList();
    }

    public FounderProfileResponseDTO getFounderProfile(Long memberId) {
        Member member = findMemberById(memberId);
        FounderProfile profile = member.getFounderProfile();
        if (profile == null) throw new EntityNotFoundException();
        return founderProfileMapper.toFounderProfileResponseDTO(profile);
    }

    @Transactional
    public FounderProfileResponseDTO createFounderProfile(Long memberId, FounderProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        FounderProfile profile = member.getFounderProfile();
        if (profile == null) {
            profile = new FounderProfile();
            profile.setMember(member);
            member.setFounderProfile(profile);
        }
        profile.setAbout(request.getAbout());
        profile.setDomains(resolveDomains(request.getDomainIds()));
        founderProfileRepository.save(profile);
        return founderProfileMapper.toFounderProfileResponseDTO(profile);
    }

    @Transactional
    public FounderProfileResponseDTO updateFounderProfile(Long memberId, FounderProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        FounderProfile profile = member.getFounderProfile();
        if (profile == null) throw new EntityNotFoundException();
        profile.setAbout(request.getAbout());
        profile.setDomains(resolveDomains(request.getDomainIds()));
        founderProfileRepository.save(profile);
        return founderProfileMapper.toFounderProfileResponseDTO(profile);
    }

    @Transactional
    public void deleteFounderProfile(Long memberId) {
        Member member = findMemberById(memberId);
        FounderProfile profile = member.getFounderProfile();
        if (profile == null) throw new EntityNotFoundException();
        member.setFounderProfile(null);
        founderProfileRepository.delete(profile);
    }

    public InvestorProfileResponseDTO getInvestorProfile(Long memberId) {
        Member member = findMemberById(memberId);
        InvestorProfile profile = member.getInvestorProfile();
        if (profile == null) throw new EntityNotFoundException();
        return investorProfileMapper.toInvestorProfileResponseDTO(profile);
    }

    @Transactional
    public InvestorProfileResponseDTO createInvestorProfile(Long memberId, InvestorProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        InvestorProfile profile = member.getInvestorProfile();
        if (profile == null) {
            profile = new InvestorProfile();
            profile.setMember(member);
            member.setInvestorProfile(profile);
        }
        profile.setAbout(request.getAbout());
        profile.setDomains(resolveDomains(request.getDomainIds()));
        investorProfileRepository.save(profile);
        return investorProfileMapper.toInvestorProfileResponseDTO(profile);
    }

    @Transactional
    public InvestorProfileResponseDTO updateInvestorProfile(Long memberId, InvestorProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        InvestorProfile profile = member.getInvestorProfile();
        if (profile == null) throw new EntityNotFoundException();
        profile.setAbout(request.getAbout());
        profile.setDomains(resolveDomains(request.getDomainIds()));
        investorProfileRepository.save(profile);
        return investorProfileMapper.toInvestorProfileResponseDTO(profile);
    }

    @Transactional
    public void deleteInvestorProfile(Long memberId) {
        Member member = findMemberById(memberId);
        InvestorProfile profile = member.getInvestorProfile();
        if (profile == null) throw new EntityNotFoundException();
        member.setInvestorProfile(null);
        investorProfileRepository.delete(profile);
    }

    public MentorProfileResponseDTO getMentorProfile(Long memberId) {
        Member member = findMemberById(memberId);
        MentorProfile profile = member.getMentorProfile();
        if (profile == null) throw new EntityNotFoundException();
        return mentorProfileMapper.toMentorProfileResponseDTO(profile);
    }

    @Transactional
    public MentorProfileResponseDTO createMentorProfile(Long memberId, MentorProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        MentorProfile profile = member.getMentorProfile();
        if (profile == null) {
            profile = new MentorProfile();
            profile.setMember(member);
            member.setMentorProfile(profile);
        }
        profile.setAbout(request.getAbout());
        profile.setDomains(resolveDomains(request.getDomainIds()));
        mentorProfileRepository.save(profile);
        return mentorProfileMapper.toMentorProfileResponseDTO(profile);
    }

    @Transactional
    public MentorProfileResponseDTO updateMentorProfile(Long memberId, MentorProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        MentorProfile profile = member.getMentorProfile();
        if (profile == null) throw new EntityNotFoundException();
        profile.setAbout(request.getAbout());
        profile.setDomains(resolveDomains(request.getDomainIds()));
        mentorProfileRepository.save(profile);
        return mentorProfileMapper.toMentorProfileResponseDTO(profile);
    }

    @Transactional
    public void deleteMentorProfile(Long memberId) {
        Member member = findMemberById(memberId);
        MentorProfile profile = member.getMentorProfile();
        if (profile == null) throw new EntityNotFoundException();
        member.setMentorProfile(null);
        mentorProfileRepository.delete(profile);
    }

    public JobSeekerProfileResponseDTO getJobSeekerProfile(Long memberId) {
        Member member = findMemberById(memberId);
        JobSeekerProfile profile = member.getJobSeekerProfile();
        if (profile == null) throw new EntityNotFoundException();
        return jobSeekerProfileMapper.toJobSeekerProfileResponseDTO(profile);
    }

    @Transactional
    public JobSeekerProfileResponseDTO createJobSeekerProfile(Long memberId, JobSeekerProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        JobSeekerProfile profile = member.getJobSeekerProfile();
        if (profile == null) {
            profile = new JobSeekerProfile();
            profile.setMember(member);
            member.setJobSeekerProfile(profile);
        }
        profile.setAbout(request.getAbout());
        profile.setSpecialization(resolveSpecialization(request.getSpecializationId()));
        jobSeekerProfileRepository.save(profile);
        return jobSeekerProfileMapper.toJobSeekerProfileResponseDTO(profile);
    }

    @Transactional
    public JobSeekerProfileResponseDTO updateJobSeekerProfile(Long memberId, JobSeekerProfileRequestDTO request) {
        Member member = findMemberById(memberId);
        JobSeekerProfile profile = member.getJobSeekerProfile();
        if (profile == null) throw new EntityNotFoundException();
        profile.setAbout(request.getAbout());
        profile.setSpecialization(resolveSpecialization(request.getSpecializationId()));
        jobSeekerProfileRepository.save(profile);
        return jobSeekerProfileMapper.toJobSeekerProfileResponseDTO(profile);
    }

    @Transactional
    public void deleteJobSeekerProfile(Long memberId) {
        Member member = findMemberById(memberId);
        JobSeekerProfile profile = member.getJobSeekerProfile();
        if (profile == null) throw new EntityNotFoundException();
        member.setJobSeekerProfile(null);
        jobSeekerProfileRepository.delete(profile);
    }

    public List<JobSeekerSpecializationResponseDTO> getJobSeekerSpecializations() {
        List<JobSeekerSpecialization> jobSeekerSpecializations = jobSeekerSpecializationRepository.findAll();
        if (jobSeekerSpecializations.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return jobSeekerSpecializationMapper.toJobSeekerSpecializationListResponse(jobSeekerSpecializations);
    }

    public JobSeekerSpecialization getJobSeekerSpecializationEntity(Long id) {
        return jobSeekerSpecializationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
