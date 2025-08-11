package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HashtagsRepository {
    private final EntityManager em;

    // 해시태그 이름으로 조회
    public Optional<Hashtags> findByName(String name) {
        List<Hashtags> list = em.createQuery(
                        "SELECT h FROM Hashtags h WHERE h.name = :name", Hashtags.class)
                .setParameter("name", name)
                .getResultList();
        return list.stream().findFirst();
    }

    // 저장
    public void save(Hashtags hashtag) {
        em.persist(hashtag);
    }
}
