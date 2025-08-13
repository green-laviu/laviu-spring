package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StreamHashtagsRepository {
    private final EntityManager em;

    // 저장
    public StreamHashtags save(StreamHashtags streamHashtag) {
        em.persist(streamHashtag);
        return streamHashtag;
    }

    public List<StreamHashtags> findAllByStreamId(Integer streamId) {
        Query query = em.createQuery("select sh from StreamHashtags sh where sh.stream.id = :streamId");
        query.setParameter("streamId", streamId);
        return query.getResultList();
    }
}
