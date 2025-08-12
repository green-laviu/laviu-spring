package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HashtagsRepository {
    private final EntityManager em;

    // 해시태그 이름으로 조회
    public Optional<Hashtags> findByName(String name) {
        try {
            Hashtags hashtag = em.createQuery(
                            "select h from Hashtags h where h.name = :name", Hashtags.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(hashtag);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // 저장
    public Hashtags save(Hashtags hashtag) {
        em.persist(hashtag);
        return hashtag;
    }
}
