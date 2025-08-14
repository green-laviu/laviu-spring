package com.metacoding.laviu.domain.users.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    public Optional<Users> getByEmailAndType(String email, UsersType type) {
        try {
            return Optional.ofNullable(em.createQuery(
                            "select u from Users u where u.email = :email and u.type = :type", Users.class)
                    .setParameter("email", email)
                    .setParameter("type", type)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * 모든 유저 목록을 조회하는 메서드.
     *
     * @return 모든 유저 엔티티 리스트
     */
    public List<Users> getAllUsers() {
        return em.createQuery("select u from Users u order by u.createdAt desc", Users.class)
                .getResultList();
    }
}
