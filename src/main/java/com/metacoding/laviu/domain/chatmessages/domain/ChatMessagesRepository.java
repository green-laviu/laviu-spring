package com.metacoding.laviu.domain.chatmessages.domain;

import com.metacoding.laviu.domain.streams.domain.Streams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatMessagesRepository {
    private final EntityManager em;

    public Optional<Streams> findByStreamId(Integer id) {
        Streams stream = em.find(Streams.class, id);
        return Optional.ofNullable(stream);
    }

    public List<ChatMessages> findAllByStreamIdJoinUser(Integer streamId) {

        String jpql = """
                    select c
                    from ChatMessages c
                    join fetch c.user u
                    where c.stream.id = :streamId
                    order by c.createdAt desc
                """;

        List<ChatMessages> list = em.createQuery(jpql, ChatMessages.class)
                .setParameter("streamId", streamId)
                .setMaxResults(30) //30개만 받아오기
                .getResultList();

        return list;
    }

    public ChatMessages save(ChatMessages chatMessages) {
        em.persist(chatMessages);
        return chatMessages;
    }

    public List<ChatMessages> findLatest30ByStreamKeyJoinFetchUserAndStream(String streamKey) {
        Query query = em.createQuery("select c from ChatMessages c join fetch c.user join fetch c.stream where c.stream.streamKey = :streamKey order by c.id desc", ChatMessages.class);
        query.setParameter("streamKey", streamKey);
        query.setMaxResults(30);
        return query.getResultList();
    }

    public List<ChatMessages> findLatest30ByStreamIdJoinFetchUserAndStream(Integer streamId) {
        Query query = em.createQuery("select c from ChatMessages c join fetch c.user join fetch c.stream where c.stream.id = :streamId order by c.id desc", ChatMessages.class);
        query.setParameter("streamId", streamId);
        query.setMaxResults(30);
        return query.getResultList();
    }
}

