package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepository {
    private final EntityManager em;

    public Optional<Users> findById(Integer userId) {
        try {
            Query query = em.createQuery("SELECT u FROM Users u WHERE u.id = :userId");
            query.setParameter("userId", userId);
            return Optional.of((Users) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
