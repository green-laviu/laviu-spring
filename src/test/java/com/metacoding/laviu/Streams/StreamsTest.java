package com.metacoding.laviu.Streams;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.service.StreamsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class StreamsTest {

    @Autowired
    private StreamsService streamsService;
    @Autowired
    private StreamsRepository streamsRepository;

    @Test
    public void save_test() {

        //given
        StreamsRequest.SaveDto reqDTO = new StreamsRequest.SaveDto("제목", "https://picsum.photos/seed/stream1/320/180");

        //when
        streamsService.createAndSaveStreamKey(reqDTO,2);
        Streams streams = streamsRepository.findById(1);
        System.out.println("id : " + streams.getId());
        System.out.println("스트리머 : " + streams.getStreamer());
        System.out.println("제목 : " + streams.getTitle());
        System.out.println("스트리머키 : " +streams.getStreamkey());




    }

}
