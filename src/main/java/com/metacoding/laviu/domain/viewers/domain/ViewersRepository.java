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

    public List<Viewers> findAllByStreamId(Integer streamId) {
        Query query = em.createQuery("select v from Viewers v join fetch v.user u where v.stream.id = :streamId", Viewers.class);
        query.setParameter("streamId", streamId);
        return query.getResultList();
    }

    public void delete(Viewers viewer) {
        em.remove(viewer);
    }

    public Optional<Viewers> findById(Integer viewerId) {
        return Optional.ofNullable(em.find(Viewers.class, viewerId));
    }

    public Optional<Viewers> findByStreamIdAndUserId(Integer streamId, Integer userId) {
        Query query = em.createQuery("select v from Viewers v where v.stream.id = :streamId and v.user.id = :userId", Viewers.class);
        query.setParameter("streamId", streamId);
        query.setParameter("userId", userId);
        try {
            return Optional.ofNullable((Viewers) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
