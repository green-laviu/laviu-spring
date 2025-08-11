package com.metacoding.laviu.domain.streams.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StreamsRepository {
    private final EntityManager em;

    //저장
    public void save(Streams stream) {
        em.persist(stream);
    }

    // live 중인 방송이 있는 지 조회 (userId로)
    public Optional<Streams> findByuserId(Users user) {

        String jpql = """
                SELECT s
                FROM Streams s
                WHERE s.streamer.id = :userId
                  AND s.status = :status
                """;

        Query query = em.createQuery(jpql, Streams.class);
        query.setParameter("userId", user.getId())
                .setParameter("status", StreamsStatus.LIVE);
        try {
            return Optional.of((Streams) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }


    }
/*
    public Optional<Streams> findByIdJoinUser(int id) {

        String jpql = """
                    SELECT s
                    FROM Streams s
                    JOIN FETCH s.streamer u
                    WHERE s.id = :id
                      AND s.status = :status
                """;

        Query query = em.createQuery(jpql, Streams.class);
        query.setParameter("id", id)
                .setParameter("status", StreamsStatus.LIVE);
        try {
            return Optional.of((Streams) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }
   */
public Optional<Streams> findByIdJoinUser(int id) {
    Streams stream = em.find(Streams.class, id);
    return Optional.ofNullable(stream);
}


}

