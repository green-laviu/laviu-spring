package com.metacoding.laviu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

// 4. 문서 만들기
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080) // build 폴더에 adoc 파일 생성
@AutoConfigureMockMvc // MockMvc 클래스가 IoC에 로드 | RestTemplate -> 자바스크립트의 fetch와 동일, 진짜 환경에 요청 가능
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
public abstract class MyRestDoc {
    @Autowired
    protected MockMvc mvc;
    protected RestDocumentationResultHandler document;

    @BeforeEach
    public void documentSetUp() {
        this.document = MockMvcRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
    }

//    // 테스트 전에 TimeValue의 시간을 특정 시점으로 고정
//    @BeforeEach
//    void setTime() {
//        // TimeValue의 내부 시간을 테스트에 맞게 조작 (필요하다면)
//        // 예를 들어, TimeValue 클래스에 테스트용 setter를 추가하여 시간을 설정할 수 있습니다.
//        TimeValue.setTestTime(LocalDateTime.of(2025, 6, 23, 0, 0, 0));
//    }
//
//    @AfterEach
//    void clearTime() {
//        // 테스트가 끝나면 설정된 시간을 초기화하여 다른 테스트에 영향을 주지 않도록 합니다.
//        TimeValue.clearTestTime();
//    }

    // mock 으로 주입 받는 가짜 검증 유틸 클래스
//    @MockitoBean
//    private RSAUtil rsaUtil;


//    @BeforeEach
//    void setup() {
//        // 1. 테스트에서 사용할 가짜 사용자 프로필 객체를 생성합니다.
//        OAuthProfile fakeProfile = new OAuthProfile();
//
//        // 2. 각 필드에 테스트에 필요한 가짜 데이터를 설정합니다.
//        //    - aud, iss: 토큰 발급자 정보, 테스트에서는 간단한 문자열로 충분합니다.
//        fakeProfile.setAud("test-app-key-1234");
//        fakeProfile.setIss("https://kauth.kakao.com");
//
//        //    - sub: 사용자의 고유 ID. 가장 중요한 필드입니다.
//        //      이 값이 User 엔티티의 loginId가 됩니다.
//        fakeProfile.setSub("123456789");
//
//        //    - nickname: 사용자 닉네임. 이 값이 User 엔티티의 username이 됩니다.
//        fakeProfile.setNickname("테스트유저");
//
//        //    - picture: 프로필 사진 URL. 필요 없다면 null로 두어도 됩니다.
//        fakeProfile.setPicture("http://example.com/profiles/test-user.jpg");
//
//        // 3. rsaUtil.verify() 메서드가 어떤 토큰으로 호출되든,
//        //    위에서 만든 가짜 프로필(fakeProfile)을 반환하도록 설정합니다.
//        when(rsaUtil.verify(anyString())).thenReturn(fakeProfile);
//    }
}