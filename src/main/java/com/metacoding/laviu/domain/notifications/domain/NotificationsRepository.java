package com.metacoding.laviu.domain.notifications.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationsRepository {
    private final EntityManager em;
}
