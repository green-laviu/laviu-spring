package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StreamsService {

    private final StreamsRepository streamsRepository;

    //save
    @Transactional
    public String save(StreamsRequest.SaveDto reqDTO, int id) {

        //1. streamkey 생성
         String streamKey = UUID.randomUUID().toString();

         //2. findbyid로 나중에 변경해서 받아오기
        Users user  = new Users(id);
        try {
            //3. stream으로 변환 (빌더)
            Streams stream = Streams.builder()
                    .streamKey(UUID.randomUUID().toString())
                    .title(reqDTO.getTitle())
                    .thumbnailUrl(reqDTO.getThumbnailUrl())
                    .status(StreamsStatus.PENDING)
                    .viewerCount(0)
                    .streamer(user)
                    .build();
            //4.save
            streamsRepository.save(stream);

        }catch (RuntimeException e){
            System.out.println("error 저장에 실패했습니다 ");
            throw new IllegalStateException("스트림 저장 실패", e);
        }

     return streamKey;
       }
    }

