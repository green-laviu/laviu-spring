package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewersRepository {
    private final EntityManager em;

    public Viewers save(Viewers viewers) {
        em.persist(viewers);
        return viewers;
    }

}
