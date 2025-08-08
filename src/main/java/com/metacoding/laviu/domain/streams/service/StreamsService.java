package com.metacoding.laviu.domain.streams.service;

import com.metacoding.laviu._core.error.ErrorEnum;
import com.metacoding.laviu._core.error.ex.ExceptionApi400;
import com.metacoding.laviu._core.error.ex.ExceptionApi403;
import com.metacoding.laviu._core.error.ex.ExceptionApi404;
import com.metacoding.laviu.domain.streams.domain.Streams;
import com.metacoding.laviu.domain.streams.domain.StreamsRepository;
import com.metacoding.laviu.domain.streams.domain.StreamsStatus;
import com.metacoding.laviu.domain.streams.dto.StreamsRequest;
import com.metacoding.laviu.domain.users.domain.Users;
import com.metacoding.laviu.domain.users.domain.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (streamKey == null || token == null) new ExceptionApi400(ErrorEnum.INVALID_TOKEN_FORMAT);

        Integer userId = 1; // getUserId(token); 추후 사용 예정 로직 밑에 구현 되어있음
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_USER));
        Streams findStream = streamsRepository.findByStreamKey(streamKey)
                .orElseThrow(() -> new ExceptionApi404(ErrorEnum.NOT_FOUND_STREAM));

        if (!findStream.getStreamer().getId().equals(userId))
            throw new ExceptionApi403(ErrorEnum.NO_MATCH_STREAMER_ID_AND_USER_ID);

        findStream.updateStatus(StreamsStatus.LIVE);
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
}
