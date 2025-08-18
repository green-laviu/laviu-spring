package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
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

    public void delete(Users users) {
        em.remove(users);
    }

    //유저 검색
    public List<Users> findAllByQuery(String query, Integer userId) {

        //1.공백 제거 , 빈 문자열 처리
        String queryResult = (query == null) ? "" : query.replaceAll(" ", "").trim();

        //2.내 정보는 리스트에 제외 + 빈문자열은 빈 리스트 반환
        String jpql = """
                select u from Users u
                 where u.id <> :userId
                 and (:query <> '' and lower(u.nickname) like concat('%', lower(:query), '%'))
                """;
        return em.createQuery(jpql, Users.class)
                .setParameter("query", queryResult)
                .setParameter("userId", userId)
                .getResultList();
    }
}
