package org.siri_hate.chat_service.repository;

import org.siri_hate.chat_service.model.entity.Chat;
import org.siri_hate.chat_service.model.enums.ChatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, JpaSpecificationExecutor<Chat> {
    @Query("""
            select c
            from Chat c
            join c.members cm
            join cm.user u
            left join c.messages m
            where u.username = :username
            group by c
            order by coalesce(max(m.sendAt), :minDate) desc, c.id desc
            """)
    Page<Chat> findMyChatsOrderedByLastMessage(
            @Param("username") String username,
            @Param("minDate") LocalDateTime minDate,
            Pageable pageable
    );

    @Query("""
            select c from Chat c
            where c.type = :chatType
            and (select count(cm) from ChatMember cm where cm.chat = c) = 2
            and exists (select 1 from ChatMember cm1 where cm1.chat = c and cm1.user.username = :u1)
            and exists (select 1 from ChatMember cm2 where cm2.chat = c and cm2.user.username = :u2)
            order by c.id asc
            """)
    List<Chat> findPrivateChatsBetweenTwoUsers(
            @Param("chatType") ChatType chatType,
            @Param("u1") String u1,
            @Param("u2") String u2
    );
}