package com.metacoding.laviu.domain.users.domain;

import com.metacoding.laviu.domain.users.dto.UsersResponse;
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
            Query query = em.createQuery("select u from Users u where u.id = :userId");
            query.setParameter("userId", userId);
            return Optional.of((Users) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public UsersResponse.StreamerDTO returnStreamerDTO(Integer userId, Integer tokenUserId) {
        Query query = em.createNativeQuery("select from detail_users_info(:userId, :tokenId)", Object[].class);
        query.setParameter("userId", userId);
        query.setParameter("tokenId", userId);

        Object[] column = (Object[]) query.getSingleResult();
        UsersResponse.StreamerDTO result = new UsersResponse.StreamerDTO(

        );
        return result;
    }
}
