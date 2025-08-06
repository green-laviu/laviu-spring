package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowsRepository {
    private final EntityManager em;
}
