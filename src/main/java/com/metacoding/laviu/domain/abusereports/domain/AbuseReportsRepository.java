package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AbuseReportsRepository {
    private final EntityManager em;

    public List<AbuseReports> findAll() {
        return em.createQuery(
                "select a from AbuseReports a order by a.createdAt desc", AbuseReports.class
        ).getResultList();
    }
    
    public Optional<AbuseReports> findById(Integer id) {
        return Optional.ofNullable(em.find(AbuseReports.class, id));
    }
}
