package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class RtmpControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;
    private String accessToken;


    @Test
    public void on_publish_test() throws Exception {
        // given
        String name = "cfy_aDktqoqESx6g1DGBEw==";
        String app = "live";

        // RTMP 서버의 on-publish 이벤트 바디 형식을 그대로 사용
        // application/x-www-form-urlencoded 형식으로 문자열을 직접 생성합니다.
        String requestBody = "app=" + app + "&name=" + name;

        System.out.println("✅요청 바디: " + requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/rtmp/on-publish")
                        // contentType을 application/x-www-form-urlencoded 로 변경
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(requestBody)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);

    }

    @Test
    public void change_thumbnails_test() throws Exception {
        //given
        String thumbnailsUrl = "testUrl";
        String streamKey = "cfy_aDktqoqESx6g1DGBEw==";
        StreamsRequest.ThumbnailUpdateDTO reqDTO = new StreamsRequest.ThumbnailUpdateDTO();
        reqDTO.setThumbnailUrl(thumbnailsUrl);

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println("✅요청 바디: " + requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/rtmp/" + streamKey + "/thumbnails")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }
}