package com.metacoding.laviu.domain.viewers.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ViewerSanctionsRepository {
    private final EntityManager em;
}
