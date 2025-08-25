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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));

        actions.andExpect(jsonPath("$.data").exists());
        actions.andExpect(jsonPath("$.data.id").value(1));
        actions.andExpect(jsonPath("$.data.title").value("자바 기초 강의"));
        actions.andExpect(jsonPath("$.data.viewerCount").value(100));
        actions.andExpect(jsonPath("$.data.status").value("LIVE"));
        actions.andExpect(jsonPath("$.data.endedAt").isEmpty());

        actions.andExpect(jsonPath("$.data.streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(jsonPath("$.data.thumbnailUrl", matchesPattern("^https://.*$")));
        actions.andExpect(jsonPath("$.data.startedAt", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")));
        actions.andExpect(jsonPath("$.data.updatedAt").isEmpty());


        actions.andExpect(jsonPath("$.data.streamer.userId").value(1));
        actions.andExpect(jsonPath("$.data.streamer.nickname").value("ssar"));
        actions.andExpect(jsonPath("$.data.streamer.profileImageUrl").value("https://plus.unsplash.com/premium_photo-1682095606317-50dec75d283c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8a29yZWF8ZW58MHx8MHx8fDA%3D"));
        actions.andExpect(jsonPath("$.data.streamer.email").value("ssar@nate.com"));
        actions.andExpect(jsonPath("$.data.streamer.bio").value("안녕하세요"));

        actions.andExpect(jsonPath("$.data.streamHashtagList").isArray());
        actions.andExpect(jsonPath("$.data.streamHashtagList[0].hashtagId").value(1));
        actions.andExpect(jsonPath("$.data.streamHashtagList[0].hashtagName").value("게임"));
        actions.andExpect(jsonPath("$.data.streamHashtagList[1].hashtagId").value(2));
        actions.andExpect(jsonPath("$.data.streamHashtagList[1].hashtagName").value("방송"));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document);

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
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));

        actions.andExpect(jsonPath("$.data").exists());
        actions.andExpect(jsonPath("$.data.id").value(1));
        actions.andExpect(jsonPath("$.data.title").value("자바 기초 강의"));
        actions.andExpect(jsonPath("$.data.viewerCount").value(100));
        actions.andExpect(jsonPath("$.data.status").value("LIVE"));
        actions.andExpect(jsonPath("$.data.endedAt").isEmpty());

        actions.andExpect(jsonPath("$.data.streamKey", matchesPattern("^[0-9a-zA-Z\\-_=]+$")));
        actions.andExpect(jsonPath("$.data.thumbnailUrl", matchesPattern("^testUrl\\?date=\\d+$")));
        actions.andExpect(jsonPath("$.data.startedAt", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")));
        actions.andExpect(jsonPath("$.data.updatedAt", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")));

        actions.andExpect(jsonPath("$.data.streamer.userId").value(1));
        actions.andExpect(jsonPath("$.data.streamer.nickname").value("ssar"));
        actions.andExpect(jsonPath("$.data.streamer.profileImageUrl").value("https://plus.unsplash.com/premium_photo-1682095606317-50dec75d283c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8a29yZWF8ZW58MHx8MHx8fDA%3D"));
        actions.andExpect(jsonPath("$.data.streamer.email").value("ssar@nate.com"));
        actions.andExpect(jsonPath("$.data.streamer.bio").value("안녕하세요"));

        actions.andExpect(jsonPath("$.data.streamHashtagList").isArray());
        actions.andExpect(jsonPath("$.data.streamHashtagList[0].hashtagId").value(1));
        actions.andExpect(jsonPath("$.data.streamHashtagList[0].hashtagName").value("게임"));
        actions.andExpect(jsonPath("$.data.streamHashtagList[1].hashtagId").value(2));
        actions.andExpect(jsonPath("$.data.streamHashtagList[1].hashtagName").value("방송"));

        actions.andDo(MockMvcResultHandlers.print())
                .andDo(document);
    }
}