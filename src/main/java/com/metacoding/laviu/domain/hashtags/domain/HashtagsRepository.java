package com.metacoding.laviu.domain.hashtags.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HashtagsRepository {
    private final EntityManager em;
}
