package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowsRepository {
    private final EntityManager em;


    public Long countByFollowingId(int id) {
        String jpql = "select count(f.id) from Follows f where f.following.id= :channelId";
        Query query = em.createQuery(jpql, Long.class);
        query.setParameter("channelId", id);
        return (Long) query.getSingleResult();


    }

    public Boolean existsByFollowerIdAndFollowingId(int streamerId, int userId) {

        String jpql = """
                    select case when count(f) > 0 then true else false end
                    from Follows f
                    where f.follower.id = :viewerId
                      and f.following.id = :channelId
                """;

        Query query = em.createQuery(jpql, Boolean.class);
        query.setParameter("viewerId", userId);
        query.setParameter("channelId", streamerId);

        try {
            return (Boolean) query.getSingleResult();
        } catch (Exception ex) {
            return false;
        }
    }
}
