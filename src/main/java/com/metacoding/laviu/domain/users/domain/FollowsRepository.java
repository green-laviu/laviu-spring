package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FollowsRepository {
    private final EntityManager em;


    //팔로워 수
    public Long countByFollowingId(int id) {
        String jpql = "select count(f.id) from Follows f where f.following.id= :channelId";
        Query query = em.createQuery(jpql, Long.class);
        query.setParameter("channelId", id);
        return (Long) query.getSingleResult();


    }

    //팔로우 중인지 확인( boolean/ 카운트 쿼리)
    public Boolean existsByFollowerIdAndFollowingId(int streamerId, int userId) {

        String jpql = """
                    select case when count(f) > 0 then true else false end
                    from Follows f
                    where f.follower.id = :userId
                      and f.following.id = :streamerId
                """;

        Query query = em.createQuery(jpql, Boolean.class);
        query.setParameter("userId", userId);
        query.setParameter("streamerId", streamerId);

        try {
            return (Boolean) query.getSingleResult();
        } catch (Exception ex) {
            return false;
        }
    }

    //팔로우 save
    public void save(Follows follow) {
        em.persist(follow);
    }

    public Optional<Follows> findByFollowerIdAndFollowingId(int streamerId, int userId) {
        String jpql = """
                    select f from Follows f
                    where f.follower.id = :userId
                      and f.following.id = :streamerId
                """;
        Query query = em.createQuery(jpql, Follows.class);
        query.setParameter("userId", userId);
        query.setParameter("streamerId", streamerId);

        return Optional.ofNullable((Follows) query.getSingleResult());
    }
}
