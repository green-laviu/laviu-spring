package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StreamHashtagsRepository {
    private final EntityManager em;

    // 저장
    public void save(StreamHashtags streamHashtag) {
        em.persist(streamHashtag);
    }
}
