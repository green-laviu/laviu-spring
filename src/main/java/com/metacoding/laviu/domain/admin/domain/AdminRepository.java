package com.metacoding.laviu.domain.admin.domain;

import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final EntityManager em;

    public Users getByEmailAndType(String email, UsersType type) {
        try {
            return em.createQuery(
                            "select u from Users u where u.email = :email and u.type = :type", Users.class)
                    .setParameter("email", email)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Users getByEmail(String email) {
        try {
            return em.createQuery(
                            "select u from Users u where u.email = :email", Users.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Users> getByType(UsersType type) {
        try {
            return em.createQuery(
                            "select u from Users u where u.type = :type", Users.class)
                    .setParameter("type", type)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
