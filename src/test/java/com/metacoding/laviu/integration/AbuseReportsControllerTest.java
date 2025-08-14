package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu.domain.abusereports.dto.AbuseReportsRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class AbuseReportsControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    public void save_test() throws Exception {
        //given
        AbuseReportsRequest.saveDTO reqDTO = new AbuseReportsRequest.saveDTO();
        reqDTO.setDetails("신고사유");
        reqDTO.setCategoryId(1);
        Integer streamId = 5;

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println("바디확인" + requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/v1/streams/{streamId}/abusereports", streamId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }


}
