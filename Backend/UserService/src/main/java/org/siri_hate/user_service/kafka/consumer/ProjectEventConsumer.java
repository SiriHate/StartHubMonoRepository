package org.siri_hate.user_service.kafka.consumer;

import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import org.siri_hate.user_service.kafka.producer.ProjectEventProducer;
import org.siri_hate.user_service.model.entity.Member;
import org.siri_hate.user_service.kafka.message.ProjectUpdateNotification;
import org.siri_hate.user_service.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectEventConsumer {

    private final MemberRepository memberRepository;
    private final ProjectEventProducer projectEventProducer;
    private final Gson gson;

    @Autowired
    public ProjectEventConsumer(
            MemberRepository memberRepository,
            ProjectEventProducer projectEventProducer,
            Gson gson
    )
    {
        this.memberRepository = memberRepository;
        this.projectEventProducer = projectEventProducer;
        this.gson = gson;
    }

    @KafkaListener(topics = "${project.update.notification.topic.consumer}", groupId = "${spring.application.name}")
    @Transactional
    public void consumeProjectUpdateNotification(String message) {
        ProjectUpdateNotification notification = gson.fromJson(message, ProjectUpdateNotification.class);
        String username = notification.getUsername();
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(EntityNotFoundException::new);
        notification.setUserRealName(member.getName());
        notification.setUserEmailAddress(member.getEmail());
        projectEventProducer.sendProjectUpdateNotification(notification);
    }
}