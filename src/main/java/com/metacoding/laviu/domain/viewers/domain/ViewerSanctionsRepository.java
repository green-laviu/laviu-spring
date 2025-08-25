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

    public Optional<ViewerSanctions> findByStreamIdAndSanctionedUserIdJoinFetchSanctionedUser(Integer streamsId, Integer sanctionedUserId) {
        Query query = em.createQuery("""
                select v
                from ViewerSanctions v
                join fetch v.sanctionedUser s
                where v.sanctionedUser.id = :sanctionedUserId
                and v.stream.id = :streamsId
                order by v.createdAt desc
                """, ViewerSanctions.class);

        query.setParameter("streamsId", streamsId);
        query.setParameter("sanctionedUserId", sanctionedUserId);
        query.setMaxResults(1);

        try {
            ViewerSanctions result = (ViewerSanctions) query.getSingleResult();
            return Optional.ofNullable(result);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public ViewerSanctions save(ViewerSanctions v) {
        em.persist(v);
        return v;
    }

    public Optional<ViewerSanctions> findByStreamIdAndSanctionedUserIdAndTypeAndIsActive(Integer streamId, Integer sanctionedUserId, ViewerSanctionsType type, Boolean isActive) {
        Query query = em.createQuery("""
                select v
                from ViewerSanctions v
                where v.stream.id = :streamId
                and v.sanctionedUser.id = :sanctionedUserId
                and v.type = :type
                and v.isActive = :isActive
                """, ViewerSanctions.class);

        query.setParameter("streamId", streamId);
        query.setParameter("sanctionedUserId", sanctionedUserId);
        query.setParameter("type", type);
        query.setParameter("isActive", isActive);

        try {
            ViewerSanctions result = (ViewerSanctions) query.getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ViewerSanctions> findByStreamKeyAndUserId(String streamKey, Integer id) {
        Query query = em.createQuery("""
                    select v
                    from ViewerSanctions v
                    where v.stream.streamKey = :streamKey
                    and v.sanctionedUser.id = :id
                    order by v.createdAt desc
                """, ViewerSanctions.class);
        query.setParameter("streamKey", streamKey);
        query.setParameter("id", id);
        query.setMaxResults(1);
        try {
            return Optional.ofNullable((ViewerSanctions) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
