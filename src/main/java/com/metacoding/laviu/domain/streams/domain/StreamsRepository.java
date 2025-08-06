package com.metacoding.laviu.domain.streams.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StreamsRepository {
    private final EntityManager em;
}
