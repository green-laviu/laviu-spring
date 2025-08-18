package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AbuseReportsRepository {
    private final EntityManager em;

    public Optional<AbuseReportCategorys> findById(Integer categoryId) {
        return Optional.ofNullable(em.find(AbuseReportCategorys.class, categoryId));
    }

    public AbuseReports save(AbuseReports abuseReport) {
        em.persist(abuseReport);
        return abuseReport;
    }


}
