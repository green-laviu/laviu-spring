package com.metacoding.laviu.domain.users.service;

import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersProvider;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import com.metacoding.laviu.domain.users.domain.UsersType;
import com.metacoding.laviu.domain.users.dto.UsersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // 네이버 OAUTH 로그인
    @Transactional
    public Object naverOauthLogin(String accessToken) {
        log.info("[NaverOauth] 네이버 로그인 요청 시작");

        // 네이버 OAuth : accessToken으로 유저 정보 요청
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UsersResponse.NaverVerifyDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                UsersResponse.NaverVerifyDTO.class
        );
        UsersResponse.NaverVerifyDTO.NaverUserInfo userInfo = response.getBody().getResponse();
        log.debug("[NaverOauth] 응답 받은 유저: email={}, mobile={}", userInfo.getEmail(), userInfo.getMobile());

        // 1. 유저 중복 확인
        Optional<Users> userOP = usersRepository.getByEmail(userInfo.getEmail());

        // 2. 기존 유저인 경우 로그인 처리
        if (userOP.isPresent()) {
            Users userPS = userOP.get();
            log.info("[NaverOauth] 기존 유저 로그인 성공: userId={}, email={}", userPS.getId(), userPS.getEmail());

            // JWT 토큰 발급
            String myAccessToken = JwtUtil.create(userPS);

            // false는 기존 유저를 의미
            return new UsersResponse.LoginDTO(userPS, myAccessToken, false);
        }

        // 3. 신규 유저인 경우 회원가입 진행
        log.info("[NaverOauth] 신규 유저입니다. 회원가입 진행: naverId={}", userInfo.getId());

        // 3-1. 비밀번호 생성 (소셜 로그인용 고정)
        String password = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());

        // 3-2. user 객체 생성
        Users newUser = Users.builder()
                .nickname(userInfo.getNickname())
                .password(password)
                .email(userInfo.getEmail())
                .profileImageUrl(userInfo.getProfile_image())
                .provider(UsersProvider.NAVER)
                .roles(UsersType.USER.name())
                .bio(null)
                .build();

        // 3-4. 유저 저장
        usersRepository.save(newUser);
        log.info("[NaverOauth] 신규 유저 저장 완료: nickname={}", userInfo.getNickname());

        // JWT 토큰 발급
        String myAccessToken = JwtUtil.create(newUser);

        // true는 신규 유저를 의미
        return new UsersResponse.LoginDTO(newUser, myAccessToken, true);
    }

}
