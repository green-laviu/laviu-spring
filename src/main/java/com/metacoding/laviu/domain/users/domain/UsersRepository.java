package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepository {
    private final EntityManager em;

    public Optional<Users> findById(Integer userId) {
        return Optional.ofNullable(em.find(Users.class, userId));
    }

    public void delete(Users users) {
        em.remove(users);
    }
}
