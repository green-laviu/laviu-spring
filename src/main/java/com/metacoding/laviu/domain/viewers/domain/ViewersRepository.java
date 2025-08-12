package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ViewersRepository {
    private final EntityManager em;


    @Transactional
    public Viewers save(Viewers viewer) {
        em.persist(viewer);
        return viewer;
    }

    public List<Viewers> findAllByStreamId(int streamId) {
        Query query = em.createQuery("select v from Viewers v where v.stream.id = :streamId", Viewers.class);
        query.setParameter("streamId", streamId);
        return query.getResultList();
    }

    public void delete(Viewers viewer) {
        em.remove(viewer);
    }

    public Optional<Viewers> findById(String viewerId) {
        return Optional.ofNullable(em.find(Viewers.class, viewerId));
    }
}
