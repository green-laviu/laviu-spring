package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu._core.utils.JwtUtil;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.matchesPattern;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class AbuseReportsControllerTest extends MyRestDoc {

    private String accessToken;

    /**
     * viewer =2번user로 user가 설정되었습니다.
     */
    @BeforeEach
    public void setUp() {
        // 테스트 시작 전에 실행할 코드
        System.out.println("setUp");
        Users cos = Users.builder()
                .id(2)
                .nickname("cos")
                .email("cos@nate.com")
                .roles("USER")
                .build();
        accessToken = JwtUtil.create(cos);
    }

    @Autowired
    private ObjectMapper om;

    @Test
    public void save_test() throws Exception {
        //given
        AbuseReportsRequest.saveDTO reqDTO = new AbuseReportsRequest.saveDTO();
        reqDTO.setDetails("신고사유");
        reqDTO.setCategoryId(2);
        Integer streamId = 1;

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println("바디확인" + requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/v1/streams/{streamId}/abusereports", streamId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.snapshotStreamTitle").value("자바 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.snapshotStreamerNickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.details").value("신고사유"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value("PENDING"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.processedAt").isEmpty());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.abuseReporterId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.abuseReporterNickname").value("cos"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.abuseReportedStreamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.abuseReportedStreamerId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.abuseReportedStreamerNickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.categoryId").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.categoryTitle").value("불법성 콘텐츠입니다."));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }


}
