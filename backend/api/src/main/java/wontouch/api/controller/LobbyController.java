package wontouch.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wontouch.api.dto.CreateRoomRequestDto;
import wontouch.api.dto.JoinRoomRequest;
import wontouch.api.dto.ResponseDto;
import wontouch.api.exception.CustomException;
import wontouch.api.exception.ExceptionResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
@Slf4j
public class LobbyController {

    private final RestTemplate restTemplate = new RestTemplate();

    // 로비서버 기본 주소
    @Value("${server.url}:${lobby.server.port}")
    private String lobbyServerUrl;

    public LobbyController() {
    }

    @GetMapping("/create/random-uuid")
    public ResponseEntity<String> createRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        return ResponseEntity.ok(uuid);
    }

    // 게임방 목록 조회(페이지네이션)
    @GetMapping("/rooms/list")
    public ResponseEntity<ResponseDto> getGameRoomList(Pageable pageable) {
        String targetUrl = lobbyServerUrl + "/api/rooms/list";

        URI uri = UriComponentsBuilder.fromHttpUrl(targetUrl)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                // 정렬 조건 추가
                .queryParam("sort", getSortParameters(pageable.getSort()))
                .build()
                .encode()
                .toUri();

        // 로비 서버로 GET 요청 보내기
        ResponseDto response = restTemplate.getForObject(uri, ResponseDto.class);

        return ResponseEntity.ok(response);
    }

    // 정렬 조건을 쿼리 파라미터로 변환하는 헬퍼 메서드
    private List<String> getSortParameters(Sort sort) {
        List<String> sortParams = new ArrayList<>();
        for (Sort.Order order : sort) {
            sortParams.add(order.getProperty() + "," + order.getDirection().name());
        }
        return sortParams;
    }

    // 게임방 생성
    @PostMapping("/create/room")
    public ResponseEntity<ResponseDto<?>> createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        ResponseDto responseDto = null;
        String targetUrl = lobbyServerUrl + "/api/rooms/create"; // 로비 서버 URL
        try {
            // 로비 서버로 roomId를 POST 요청으로 전송
            responseDto = restTemplate.postForObject(targetUrl, createRoomRequestDto, ResponseDto.class);
            log.info("Room ID sent to Lobby Server: {}", createRoomRequestDto.getRoomId());
        } catch (Exception e) {
            log.error("Failed to send Room ID to Lobby Server: {}", e.getMessage());
            throw new ExceptionResponse(CustomException.TRANSFER_FAILURE_EXCEPTION);
        }

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 특정 게임방 입장
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody JoinRoomRequest joinRoomRequest) {
        String url = String.format("%s/api/rooms/%s/join", lobbyServerUrl, roomId);
        try {
            ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, joinRoomRequest, ResponseDto.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
//          일단 뭉뚱그린 예외 처리
            e.printStackTrace();
            throw new ExceptionResponse(CustomException.TRANSFER_FAILURE_EXCEPTION);
        }
    }
}
