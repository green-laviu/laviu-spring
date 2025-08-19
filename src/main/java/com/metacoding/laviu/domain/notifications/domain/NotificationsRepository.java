package com.metacoding.laviu.domain.notifications.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationsRepository {
    private final EntityManager em;

    //전체 목록조회
    public List<Notifications> findAll(Integer userId) {

        // 읽지 않는 알림만 조회하기 (isRead = false)
        Query query = em.createQuery("select n from Notifications n where n.user.id = :id and n.isRead = false", Notifications.class);
        query.setParameter("id", userId);
        return query.getResultList();
    }

    //알림 저장
    public Notifications save(Notifications notification) {
        em.persist(notification);
        return notification;
    }

    // id로 조회
    public Optional<Notifications> findById(Integer notificationId) {
        return Optional.ofNullable(em.find(Notifications.class, notificationId));
    }
}
