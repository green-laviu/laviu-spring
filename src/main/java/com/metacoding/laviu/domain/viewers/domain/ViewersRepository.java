package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ViewersRepository {
    private final EntityManager em;

    @Transactional
    public Viewers save(Viewers viewers) {
        em.persist(viewers);
        return viewers;
    }

}
