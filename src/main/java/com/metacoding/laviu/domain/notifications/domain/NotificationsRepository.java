package com.metacoding.laviu.domain.notifications.domain;

import com.metacoding.laviu.domain.notifications.dto.NotificationsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationsRepository {
    private final EntityManager em;

    public List<Notifications> findAll(Integer userId) {

        Query query = em.createQuery("select n from Notifications n where n.user.id = :id",Notifications.class);
        query.setParameter("id", userId);
        List resultList = query.getResultList();
        return resultList;
    }

    public Notifications save(Notifications notification) {
        em.persist(notification);
        return notification;
    }
}
