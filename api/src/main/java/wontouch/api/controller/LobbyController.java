package wontouch.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import wontouch.api.dto.CreateRoomRequestDto;
import wontouch.api.dto.ResponseDto;
import wontouch.api.dto.RoomResponseDto;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
@Slf4j
public class LobbyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.url}:${lobby.server.port}")
    private String lobbyServerUrl;

    public LobbyController() {
    }

    @GetMapping("/create/random-uuid")
    public ResponseEntity<String> createRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        return ResponseEntity.ok(uuid);
    }

    @GetMapping("/list")
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

    @PostMapping("/create/room")
    public ResponseEntity<ResponseDto> createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        String targetUrl = lobbyServerUrl + "/api/rooms/create"; // 로비 서버 URL
        try {
            // 로비 서버로 roomId를 POST 요청으로 전송
            restTemplate.postForObject(targetUrl, createRoomRequestDto, String.class);
            log.info("Room ID sent to Lobby Server: {}", createRoomRequestDto.getRoomId());
        } catch (Exception e) {
            log.error("Failed to send Room ID to Lobby Server: {}", e.getMessage());
        }

        return ResponseEntity.created(URI.create(targetUrl)).body(
            null
        );
    }
}
