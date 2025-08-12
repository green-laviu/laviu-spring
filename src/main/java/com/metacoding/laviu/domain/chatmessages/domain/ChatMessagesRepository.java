package com.metacoding.laviu.domain.chatmessages.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatMessagesRepository {
    private final EntityManager em;

    public Optional<Streams> findByStreamId(int id) {
        Streams stream = em.find(Streams.class, id);
        return Optional.ofNullable(stream);
    }


    public List<ChatMessages> findAllByStreamIdJoinUser(int id) {

        String jpql = """
                    select c
                    from ChatMessages c
                    join fetch c.user u
                    where c.stream.id = :streamId
                    order by c.createdAt desc, c.id desc
                """;

        List<ChatMessages> list = em.createQuery(jpql, ChatMessages.class)
                .setParameter("streamId", id)
                .setMaxResults(30) //30개만 받아오기
                .getResultList();

        return list;
    }
}

