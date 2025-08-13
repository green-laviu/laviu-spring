package com.metacoding.laviu.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.laviu.MyRestDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // MOCK -> 가짜 환경을 만들어 필요한 의존관계를 다 메모리에 올려서 테스트
@Slf4j
class SampleTest extends MyRestDoc {

    @Autowired
    private ObjectMapper om;

/*    @Test
    public void update_test() throws Exception {
        // given
        Integer id = 1;
        BoardRequest.UpdateDTO reqDTO = new BoardRequest.UpdateDTO();
        reqDTO.setTitle("제1");
        reqDTO.setContent("내1");
        reqDTO.setIsPublic(true);

        String requestBody = om.writeValueAsString(reqDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/s/api/board/{id}", id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("성공"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.id").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("제1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.content").value("내1"));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.isPublic").value(true));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.userId").value(1));
        actions.andExpect(MockMvcResultMatchers.jsonPath("$.body.createdAt",
                matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+")));
        actions.andDo(MockMvcResultHandlers.print()).andDo(document);
    }*/
}