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

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
public class RtmpControllerTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

    @Test
    public void on_publish_test() throws Exception {
        //given
        String token = "123";
        String name = "abc123";
        StreamsRequest.StreamsVerifyDTO reqDTO = new StreamsRequest.StreamsVerifyDTO();
        reqDTO.setName(name);
        reqDTO.setArgs("token=" + token);

        String requestBody = om.writeValueAsString(reqDTO);

        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/rtmp/on-publish")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        );

        //eye
        String str = actions.andReturn().getResponse().getContentAsString();
        System.out.println("result : " + str);
    }

    @Test
    public void change_thumbnails_test() throws Exception {
        //given
        String thumbnailsUrl = "testUrl";
        String streamKey = "abc123";
        StreamsRequest.ThumbnailUpdateDTO reqDTO = new StreamsRequest.ThumbnailUpdateDTO();
        reqDTO.setThumbnailUrl(thumbnailsUrl);

        String requestBody = om.writeValueAsString(reqDTO);
        //when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/rtmp/" + streamKey + "/thumbnails")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //eye
        String str = actions.andReturn().getResponse().getContentAsString();
        System.out.println("result : " + str);
    }
}
