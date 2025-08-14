package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AbuseReportsRepository {
    private final EntityManager em;

    public List<AbuseReports> findAllOrderByCreatedAtDesc() {
        Query query = em.createQuery(
                "select a from AbuseReports a order by a.createdAt desc", AbuseReports.class
        );
        return query.getResultList();
    }
}
