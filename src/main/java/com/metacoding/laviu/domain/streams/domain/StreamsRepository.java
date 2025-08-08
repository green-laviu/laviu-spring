package com.metacoding.laviu.domain.streams.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StreamsRepository {
    private final EntityManager em;

    //저장
    public void save(Streams stream) {
        em.persist(stream);
    }

    //조회 (1개)
    public Streams findById(int id) {
        Query query = em.createQuery("select s from Streams s where s.id = :id");
        query.setParameter("id",id);
        return (Streams) query.getSingleResult();

    }
}
