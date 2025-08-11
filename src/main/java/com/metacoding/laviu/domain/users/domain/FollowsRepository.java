package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowsRepository {
    private final EntityManager em;


    public Long countByFollowingId(Users streamer) {
        String jpql = "select count(f.id) from Follows f where f.follower.id= :channelId";
        Query query = em.createQuery(jpql, Long.class);
        query.setParameter("channelId", streamer.getId());
        return (Long) query.getSingleResult();


    }

    public boolean existsByFollowerIdAndFollowingId(Users streamer, Users user) {

        String jpql = """
                    select case when count(f) > 0 then true else false end
                    from Follows f
                    where f.follower.id = :viewerId
                      and f.following.id = :channelId
                """;

        TypedQuery<Boolean> query = em.createQuery(jpql, Boolean.class);
        query.setParameter("viewerId", user.getId());
        query.setParameter("channelId", streamer.getId());

        return  query.getSingleResult();
    }
}
