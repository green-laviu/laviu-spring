package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ViewerSanctionsRepository {
    private final EntityManager em;

    public Optional<ViewerSanctions> findByStreamIdAndSanctionedUserId(Integer streamsId, Integer sanctionedUserId) {
        Query query = em.createQuery("""
                select v 
                from ViewerSanctions v 
                where v.sanctionedUser.id = :sanctionedUserId 
                and v.stream.id = :streamsId 
                order by v.createAt desc limit 1
                """
        );
        query.setParameter("streamsId", streamsId);
        query.setParameter("sanctionedUserId", sanctionedUserId);
        return Optional.ofNullable((ViewerSanctions) query.getSingleResult());
    }

    public ViewerSanctions save(ViewerSanctions v) {
        em.persist(v);
        return v;
    }
}
