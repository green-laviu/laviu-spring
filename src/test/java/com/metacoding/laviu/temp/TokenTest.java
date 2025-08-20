package com.metacoding.laviu.temp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.metacoding.laviu.domain.users.domain.Users;
import org.junit.jupiter.api.Test;

import java.sql.Date;

public class TokenTest {

    @Test
    public void create_test() {
        // given
        Users user = Users.builder()
                .id(5)
                .email("testStreamer@nate.com")
                .roles("USER")
                .nickname("testStreamer")
                .build();

        // when
        String jwt = JWT.create()
                .withSubject("laviu") // 토큰 이름 필수!
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 만료시간
                .withClaim("id", user.getId()) // 넣고 싶은 정보
                .withClaim("nickname", user.getNickname()) // 넣고 싶은 정보
                .withClaim("email", user.getEmail()) // 넣고 싶은 정보
                .sign(Algorithm.HMAC512("메타코딩시크릿키")); // 마지막 해쉬생성

        // eye
        System.out.println(jwt);
    }

    @Test
    public void verify() {
        // given
        Users user = Users.builder()
                .id(1)
                .email("ssar@nate.com")
                .nickname("ssar")
                .build();

        // when
        String jwt = JWT.create()
                .withSubject("laviu") // 토큰 이름 필수!
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 만료시간
                .withClaim("id", user.getId()) // 넣고 싶은 정보
                .withClaim("username", user.getUsername()) // 넣고 싶은 정보
                .withClaim("email", user.getEmail()) // 넣고 싶은 정보
                .sign(Algorithm.HMAC512("메타코딩시크릿키")); // 마지막 해쉬생성

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("메타코딩시크릿키")).build().verify(jwt);
        Integer id = decodedJWT.getClaim("id").asInt();
        String username = decodedJWT.getClaim("username").asString();

        System.out.println(id);
        System.out.println(username);
    }
}