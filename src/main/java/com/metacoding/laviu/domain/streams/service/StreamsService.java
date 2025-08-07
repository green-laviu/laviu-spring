package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StreamsService {

    private final StreamsRepository streamsRepository;
    public String createAndSaveStreamKey(StreamsRequest.SaveDto reqDTO, int id) {


        //streamkey 생성
         String streamKey = UUID.randomUUID().toString();

         //findbyid로 나중에 변경해서 받아오기
        Users streamer  = new Users(id);

        //stream으로 변환
        Streams stream = Streams.builder()
                .streamkey(UUID.randomUUID().toString())
                .title(reqDTO.getTitle())
                .thumbnailUrl(reqDTO.getThumbnailUrl())
                .status(StreamsStatus.PENDING)
                .viewerCount(0)
                .streamer(streamer)
                .build();
        //
        streamsRepository.save(stream);


     return streamKey;
       }
    }

