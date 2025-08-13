package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepository {
    private final EntityManager em;

    public Optional<Users> findById(Integer userId) {
        return Optional.ofNullable(em.find(Users.class, userId));
    }

    //유저 검색
    public List<Users> findByQuery(String query) {

        //null이나 빈값이면
        if (query == null || query.trim().isEmpty()) {

            // 검색어 없으면 전체 유저 조회
            String jpql = "select u from Users u";
            return em.createQuery(jpql, Users.class).getResultList();
        }

        //검색 쿼리문
        String jpql = """
                select u from Users u
                 where u.nickname like :query
                """;
        return em.createQuery(jpql, Users.class)
                .setParameter("query", "%" + query + "%")
                .getResultList();
    }
}
