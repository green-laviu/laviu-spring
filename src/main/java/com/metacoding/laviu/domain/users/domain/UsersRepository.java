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
        Query query = em.createNativeQuery(
                /* 스트리머의 식별자, 닉네임, 프로필 URL, 팔로워 수, 소개글, 요청자의 팔로우 여부, 방송 여부, */
                "select u.id as streamer_id, u.nickname as streamer_name, u.profile_image_url as streamer_profile_image_url," +
                        " (select COUNT(*) from follows_tb f where f.following_id = u.id) as follower_count, " +
                        "  u.bio as introduction," +
                        "  case when EXISTS(" +
                        " select 1 from follows_tb f2" +
                        " where f2.follower_id = :tokenId and f2.following_id = u.id" +
                        " ) THEN 1 ELSE 0 end as is_following, " +
                        " coalesce(" +
                        /* LIVE 우선 */
                        " (select s" +
                        " from streams_tb s" +
                        " where s.streamer_id = u.id and s.status = 'LIVE'" +
                        " order by s.started_at desc" +
                        " limit 1)," +
                        /* 없으면 최근 스트림 status */
                        " (select s2.statu" +
                        " from streams_tb s2" +
                        " where s2.streamer_id = u.id" +
                        " order by s2.started_at desc" +
                        " limit 1)," +
                        /* 둘 다 없으면 ENDED */
                        " 'ENDED'" +
                        " ) as stream_status" +
                        "from users_tb u" +
                        "where u.id = :userId;", Object[].class);
        query.setParameter("userId", userId);
        query.setParameter("tokenId", userId);

        Object[] column = (Object[]) query.getSingleResult();
        UsersResponse.StreamerDTO result = new UsersResponse.StreamerDTO(

        );
        return result;
    }
}
