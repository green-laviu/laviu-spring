package com.metacoding.laviu.domain.streams.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StreamsRepository {
    private final EntityManager em;

    public Optional<Streams> findByStreamKey(String streamKey) {
        Query query = em.createQuery("select s from Streams s where s.streamKey = :streamKey");
        query.setParameter("streamKey", streamKey);
        try {
            return Optional.of((Streams) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //저장
    public Streams save(Streams stream) {
        em.persist(stream);
        return stream;
    }

    // live 중인 방송이 있는 지 조회 (userId로)
    public Optional<Streams> findByUserIdAndLive(int userId) {

        String jpql = """
                SELECT s
                FROM Streams s
                WHERE s.streamer.id = :userId
                  AND s.status = :status
                """;

        Query query = em.createQuery(jpql, Streams.class);
        query.setParameter("userId", userId)
                .setParameter("status", StreamsStatus.LIVE);
        try {
            return Optional.of((Streams) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    public Optional<Streams> findByIdJoinStreamer(int streamId) {
        Query query = em.createQuery("select s from Streams s join fetch s.streamer where s.id = :streamId")
                .setParameter("streamId", streamId);
        try {
            return Optional.of((Streams) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Streams> findByStatusOrderByViewerCountDesc(StreamsStatus status) {
        Query query = em.createQuery("select s from Streams s left join fetch s.streamer left join fetch s.streamHashtagList sh left join fetch sh.hashtag where s.status = :status order by s.viewerCount desc", Streams.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public Optional<Streams> findById(Integer streamId) {
        return Optional.ofNullable(em.find(Streams.class, streamId));
    }

    public List<Streams> findAllByUserIdsAndStatus(Collection<Integer> userIds, StreamsStatus streamsStatus) {
        Query query = em.createQuery("select s from Streams s where s.streamer.id in :userIds and s.status = :status", Streams.class);
        query.setParameter("userIds", userIds);
        query.setParameter("status", streamsStatus);
        return query.getResultList();
    }

    //스트림 검색
    public List<Streams> findAllByQuery(String query) {

        //공백 제거 , 빈 문자열 처리
        String queryResult = (query == null) ? "" : query.replaceAll(" ", "").trim();

        //검색 쿼리
        String jpql = """
                select distinct s
                from Streams s
                join  s.streamHashtagList sh
                join  sh.hashtag h
                where s.status = :live
                and (:query <> '' and lower(h.name) like concat('%', lower(:query), '%'))
                """;

        return em.createQuery(jpql, Streams.class)
                .setParameter("query", queryResult)
                .setParameter("live", StreamsStatus.LIVE)
                .getResultList();
    }

    public boolean existsByStreamKeyAndUserId(String streamKey, Integer userId) {
        Query query = em.createQuery("select s from Streams s where s.streamKey = :streamKey and s.streamer.id = :userId");
        query.setParameter("streamKey", streamKey);
        query.setParameter("userId", userId);
        try {
            // 결과가 하나라도 있으면 성공한 것이므로 true 반환
            query.getSingleResult();
            return true;
        } catch (Exception e) {
            // 결과가 없거나 다른 예외 발생 시 false 반환
            return false;
        }
    }

    // 관리자 페이지에서 사용할 모든 스트림 목록 조회 (LIVE 상태만)
    public List<Streams> findAllLiveStreamsWithStreamer() {
        return em.createQuery(
                        "select s from Streams s join fetch s.streamer where s.status = :status order by s.startedAt desc", Streams.class
                ).setParameter("status", StreamsStatus.LIVE)
                .getResultList();
    }
}

