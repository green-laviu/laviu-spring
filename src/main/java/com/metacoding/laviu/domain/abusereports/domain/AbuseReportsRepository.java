package com.metacoding.laviu.domain.abusereports.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AbuseReportsRepository {
    private final EntityManager em;
}
