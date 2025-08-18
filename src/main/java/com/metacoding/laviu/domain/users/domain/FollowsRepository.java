package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FollowsRepository {
    private final EntityManager em;


    //팔로워 수
    public Long countByFollowingId(Integer streamerId) {
        String jpql = "select count(f.id) from Follows f where f.following.id= :streamerId";
        Query query = em.createQuery(jpql, Long.class);
        query.setParameter("streamerId", streamerId);
        return (Long) query.getSingleResult();


    }

    //팔로우 중인지 확인( boolean/ 카운트 쿼리)
    public Boolean existsByFollowerIdAndFollowingId(Integer streamerId, Integer userId) {

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
    public Follows save(Follows follow) {
        em.persist(follow);
        return follow;
    }

    //id로 조회하기
    public Optional<Follows> findByIdAndUserId(Integer followId, Integer userId) {

        Query query = em.createQuery("select f from Follows f where f.id = :followId and f.follower.id = :userId", Follows.class);
        query.setParameter("followId", followId);
        query.setParameter("userId", userId);

        try {
            return Optional.of((Follows) query.getSingleResult()); // 여기선 null 안 옴
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    public void delete(Follows follow) {
        em.remove(follow);
    }

    public Optional<Follows> findById(Integer followId) {
        return Optional.ofNullable(em.find(Follows.class, followId));
    }

    public List<Follows> findAllByIdAndNotify(Integer followingId) {
        Query query = em.createQuery("select f from Follows f where f.following.id = :followingId and f.isNotificationsEnabled =true", Follows.class);
        query.setParameter("followingId", followingId);
        List resultList = query.getResultList();
        return resultList;

    }
}
