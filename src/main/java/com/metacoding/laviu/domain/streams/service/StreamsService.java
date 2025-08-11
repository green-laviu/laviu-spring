package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu._core.utils.StreamKeyGenerator;
import com.metacoding.laviu.domain.hashtags.domain.Hashtags;
import com.metacoding.laviu.domain.hashtags.domain.StreamHashtags;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.streams.dto.StreamsResponse;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.metacoding.laviu._core.error.ErrorEnum.ALREADY_LIVE_STREAMING;

@RequiredArgsConstructor
@Service
public class StreamsService {
    private final StreamsRepository streamsRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public void verify(StreamsRequest.StreamsVerifyDTO reqDTO) {
        /*
             혹여나 문제가 생길 시, 존재하는 키의 에 대한 값 확인용
        System.out.print("[onPublish 요청]");
        for (String key : params.keySet()) {
            System.out.println(key + " : " + params.get(key));
        }
        */
        String streamKey = reqDTO.getName();
        String args = reqDTO.getArgs();
        Map<String, String> queryMap = parseQueryString(args);
        String token = queryMap.get("token");
        // 키와 토큰 조회
        if (streamKey == null || token == null)
            throw new ExceptionApi400(ErrorEnum.INVALID_TOKEN_FORMAT);

        Integer usersId = 1; // getUserId(token); 추후 사용 예정 로직 밑에 구현 되어있음
        // Entity 확인
        usersRepository.findById(usersId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_USER));
        Streams streamsPS = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_STREAM));
        // 유저 정보와 조회
        if (!streamsPS.getStreamer().getId().equals(usersId))
            throw new ExceptionApi403(ErrorEnum.NO_MATCH_STREAMER_ID_AND_USER_ID);

        streamsPS.updateStatus(StreamsStatus.LIVE);
    }

    private Map<String, String> parseQueryString(String query) {
        if (query == null || query.isBlank()) return Map.of();

        return Arrays.stream(query.split("&"))
                .map(kv -> kv.split("=", 2))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
    }
//    private Long getUserId(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY.getBytes())   // secret_key 설정 요망
//                    .parseClaimsJws(token)
//                    .getBody();
//            isExpired(claims);
//            return claims.get("userId", Long.class);
//        } catch (Exception e) {
//            System.out.println("JWT 파싱 실패: " + e.getMessage());
//            throw new ExceptionApi401(ErrorEnum.INVALID_TOKEN_FORMAT);
//        }
//    }
//
//    private void isExpired(Claims claims) throws RuntimeException {
//        if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
//            throw new ExceptionApi401(ErrorEnum.TOKEN_EXPIRED);
//        }
//    }

    //save
    @Transactional
    public StreamsResponse.SaveDTO save(StreamsRequest.SaveDTO reqDTO, Users user) {

        // 1.streams 테이블에 user 아이디로 live 인 row가 있는지 확인 없으면 정상 있으면 예외
        Optional<Streams> streamOP = streamsRepository.findByUserId(user);
        if (streamOP.isPresent()) throw new ExceptionApi400(ALREADY_LIVE_STREAMING);

        //2. streamkey 생성
        String streamKey = StreamKeyGenerator.generate();

        //3. 스트림 저장(엔티티 반환 후 저장)
        Streams stream = reqDTO.toEntity(user, streamKey);
        streamsRepository.save(stream);

        //4.hashtags save TODO
        //4-1 더미데이터 삭제 요망 (연관된 생성자도 삭제필요)
        List<StreamHashtags> streamHashtags = List.of(
                new StreamHashtags(stream, new Hashtags("소통")),
                new StreamHashtags(stream, new Hashtags("게임"))
        );

        //5. 응답 dto로 반환
        return new StreamsResponse.SaveDTO(stream, streamHashtags);
    }

    /**
     * 방송 종료하는 서비스
     */
    @Transactional
    public void delete(Integer streamsId, Integer usersId) {
        // 방송 없으면 터짐
        Streams streamsPS = streamsRepository.findById(streamsId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_STREAM));
        // 권한 없음
        if (!streamsPS.getStreamer().getId().equals(usersId))
            throw new ExceptionApi403(ErrorEnum.NO_MATCH_STREAMER_ID_AND_USER_ID);
        // 이미 종료된 방송
        if (streamsPS.getStatus() == StreamsStatus.ENDED)
            throw new ExceptionApi400(ErrorEnum.STREAM_ENDED_STATE);
        // 방송 종료
        streamsPS.off(StreamsStatus.ENDED);
    }

    @Transactional
    public void updateThumbnail(String streamKey, StreamsRequest.ThumbnailUpdateDTO reqDTO) {
        Streams streamsPS =
                streamsRepository.findByStreamKey(streamKey)
                        .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_STREAM));
        streamsPS.updateThumbnailUrl(reqDTO.getThumbnailUrl());
    }
}
