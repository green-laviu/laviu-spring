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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;

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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamId").value(4));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.streamKey",
                matchesPattern("^[0-9a-fA-F\\-]{36}$")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("방송타이틀"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value("PENDING"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.hashtagList[0].hashtagId").value(3));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.hashtagList[0].hashtagName").value("게임1"));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
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
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.streamId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.title").value("자바 기초 강의"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.streamer.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.streamer.nickname").value("ssar"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.streamer.profileImageUrl")
                .value("https://nate.com/profile1.jpg"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.streamer.email").value("ssar@nate.com"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.streamer.bio").value("안녕하세요"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.followerCount").value(2));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.channel.isFollowing").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.hlsUrl").value("http://host/hls/abc123.m3u8"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.viewerCount").value(101));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.startedAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.hashtagList[0].hashtagId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.live.hashtagList[0].hashtagName").value("게임"));
        //빈배열
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.chatList", hasSize(0)));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.data.viewerId").value(2));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
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
