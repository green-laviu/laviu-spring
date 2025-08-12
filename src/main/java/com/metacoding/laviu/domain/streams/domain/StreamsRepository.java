package com.metacoding.laviu.domain.streams.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StreamsRepository {
    private final EntityManager em;

    public Optional<Streams> findByStreamKey(String streamKey) {
        try {
            Query query = em.createQuery("SELECT s FROM Streams s WHERE s.streamKey = :streamKey");
            query.setParameter("streamKey", streamKey);
            return Optional.of((Streams) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //저장
    public void save(Streams stream) {
        em.persist(stream);
    }

    // live 중인 방송이 있는 지 조회 (userId로)
    public Optional<Streams> findByUserId(Users user) {

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

    public Optional<Streams> findById(Integer streamId) {
        try {
            Query query =
                    em.createQuery("select s from Streams s where s.id = :streamId")
                            .setParameter("streamId", streamId);
            return Optional.of((Streams) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Streams> findByStatusOrderByViewerCountDesc(StreamsStatus status) {
        Query query = em.createQuery("select s from Streams s left join fetch s.streamer left join fetch s.streamHashtags sh left join fetch sh.hashtag where s.status = :status order by s.viewerCount desc", Streams.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}

