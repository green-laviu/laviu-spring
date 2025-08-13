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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class StreamsControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    public void end_test() throws Exception {
        //given
        Integer streamId = 1;

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/streams/" + streamId + "/end")
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void save_test() throws Exception {
        //given
        StreamsRequest.SaveDTO reqDTO = new StreamsRequest.SaveDTO();
        reqDTO.setTitle("방송타이틀");
        reqDTO.setHashtagList(List.of("게임1", "방송1"));

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/s/api/v1/streams/start")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void get_test() throws Exception {
        //given
        Integer streamId = 1;

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/streams/{streamId}", streamId)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void get_streams_list_test() throws Exception {
        //given
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/s/api/v1/streams")
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
    }

    @Test
    public void update_test() throws Exception {
        //given
        Integer streamId = 1;

        StreamsRequest.UpdateDTO reqDTO = new StreamsRequest.UpdateDTO();
        reqDTO.setTitle("방송타이틀5");
        reqDTO.setHashtagList(List.of("게임5", "방송5"));

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/v1/streams/{streamId}/setting", streamId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("✅응답바디 : " + responseBody);

        //then
        
    }
}
