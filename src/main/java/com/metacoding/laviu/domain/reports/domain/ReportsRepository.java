package com.metacoding.laviu.domain.reports.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportsRepository {
    private final EntityManager em;
}
