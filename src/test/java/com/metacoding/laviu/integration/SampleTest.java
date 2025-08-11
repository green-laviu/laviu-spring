package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
class SampleTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    @DisplayName("컨트롤러 테스트 이름")
    void get_challenges_test() throws Exception {
        // given

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/community/challenges")
                        .header("Authorization", "Bearer " + "fakeToken")
        );

        //        ResultActions actions = mvc.perform(
//                MockMvcRequestBuilders
//                        .post("/s/api/community/challenges")
//                        .content(requestBody)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + fakeToken));

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        log.debug("✅응답 바디: " + responseBody);

        // then
// 최상위 응답
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));

// inviteChallenges[0]
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInviteId").value(1));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].fromUsername").value("leo"));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.id").value(1));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.name").value("6월 5k 챌린지"));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.sub").value("이번 주 5km를 달려보세요."));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.remainingTime").value(691199));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.myDistance").value(nullValue()));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.targetDistance").value(5000));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.isInProgress").value(true));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.startDate").value(Matchers.matchesRegex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.endDate").value(Matchers.matchesRegex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")));
        actions.andExpect(jsonPath("$.data.inviteChallenges[0].challengeInfo.type").value("공개"));

// recommendedChallenge
        actions.andExpect(jsonPath("$.data.recommendedChallenge.id").isNumber());
        actions.andExpect(jsonPath("$.data.recommendedChallenge.name").isString());
        actions.andExpect(jsonPath("$.data.recommendedChallenge.participantCount").isNumber());
        actions.andExpect(jsonPath("$.data.recommendedChallenge.type").isString());

// myChallenges[0]
        actions.andExpect(jsonPath("$.data.myChallenges[0].id").value(1));
        actions.andExpect(jsonPath("$.data.myChallenges[0].name").value("6월 5k 챌린지"));
        actions.andExpect(jsonPath("$.data.myChallenges[0].sub").value(nullValue()));
        actions.andExpect(jsonPath("$.data.myChallenges[0].remainingTime").value(691199));
        actions.andExpect(jsonPath("$.data.myChallenges[0].myDistance").value(17600));
        actions.andExpect(jsonPath("$.data.myChallenges[0].targetDistance").value(5000));
        actions.andExpect(jsonPath("$.data.myChallenges[0].isInProgress").value(true));
        actions.andExpect(jsonPath("$.data.myChallenges[0].endDate").value(nullValue()));
        actions.andExpect(jsonPath("$.data.myChallenges[0].type").value("공개"));

// joinableChallenges[0]
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].id").value(2));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].name").value("6월 15k 챌린지"));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].sub").value("6월 한 달 동안 15km를 달성해보세요!"));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].remainingTime").value(691199));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].myDistance").value(nullValue()));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].targetDistance").value(nullValue()));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].isInProgress").value(true));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].endDate").value(nullValue()));
        actions.andExpect(jsonPath("$.data.joinableChallenges[0].type").value("공개"));

// pastChallenges
        actions.andExpect(jsonPath("$.data.pastChallenges").isArray());
        actions.andExpect(jsonPath("$.data.pastChallenges").isEmpty());

        // 디버깅 및 문서화 (필요시 주석 해제)
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }
    
}