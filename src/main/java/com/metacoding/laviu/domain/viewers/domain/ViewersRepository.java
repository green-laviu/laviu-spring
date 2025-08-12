package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ViewersRepository {
    private final EntityManager em;

    @Transactional
    public Viewers save(Viewers viewer) {
        em.persist(viewer);
        return viewer;
    }

    public List<Viewers> findAllByStreamId(int id) {
        Query query = em.createQuery("select v from Viewers v where v.stream.id = :id", Viewers.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
