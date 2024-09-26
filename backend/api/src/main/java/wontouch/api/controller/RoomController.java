package wontouch.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import wontouch.api.dto.CreateRoomRequestDto;
import wontouch.api.dto.RoomRequestDto;
import wontouch.api.dto.ResponseDto;
import wontouch.api.dto.gameserver.PlayerRequestDto;
import wontouch.api.exception.CustomException;
import wontouch.api.exception.ExceptionResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/room")
@Slf4j
public class RoomController {

    private final RestTemplate restTemplate = new RestTemplate();

    // 로비서버 기본 주소
    @Value("${lobby.server.name}:${lobby.server.path}")
    private String lobbyServerUrl;

    // 게임서버 기본 주소
    @Value("${game.server.name}:${game.server.path}")
    private String gameServerUrl;

    public RoomController() {
    }

    @GetMapping("/create/random-uuid")
    public ResponseEntity<ResponseDto<?>> createRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.OK.value())
                .message("UUID 정상 발급 완료")
                .data(uuid)
                .build();
        return ResponseEntity.ok(responseDto);
    }

    // 게임방 생성
    @PostMapping()
    public ResponseEntity<ResponseDto<?>> createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        ResponseDto responseDto = null;
        String targetUrl = lobbyServerUrl + "/rooms/create"; // 로비 서버 URL
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
    @PostMapping("/join/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody RoomRequestDto roomRequestDto) {
        String url = String.format("%s/rooms/join/%s", lobbyServerUrl, roomId);
        try {
            ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, roomRequestDto, ResponseDto.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
//          일단 뭉뚱그린 예외 처리
            e.printStackTrace();
            throw new ExceptionResponse(CustomException.TRANSFER_FAILURE_EXCEPTION);
        }
    }

    // 특정 게임방 퇴장
    @PostMapping("/exit/{roomId}")
    public ResponseEntity<?> exitRoom(@PathVariable String roomId, @RequestBody RoomRequestDto roomRequestDto) {
        String url = String.format("%s/rooms/exit/%s", lobbyServerUrl, roomId);
        try {
            ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, roomRequestDto, ResponseDto.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException e) {
//          일단 뭉뚱그린 예외 처리
            e.printStackTrace();
            throw new ExceptionResponse(CustomException.TRANSFER_FAILURE_EXCEPTION);
        }
    }

    // 특정 게임방 시작
    @GetMapping("/start/{roomId}")
    public ResponseEntity<ResponseDto<?>> getPlayersForStartGame(@PathVariable String roomId) {
        String targetUrl = String.format("%s/ready/start/%s", lobbyServerUrl, roomId);

        ResponseEntity<ResponseDto<?>> response = restTemplate.exchange(
                targetUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseDto<?>>() {}
        );
        return response;
    }

    @PostMapping("/start/{roomId}")
    public ResponseEntity<?> initializeGamePlayer(@PathVariable String roomId, @RequestBody List<PlayerRequestDto> players) {
        String targetUrl = String.format("%s/game/init/%s", gameServerUrl, roomId);
        log.debug("targetUrl:{}", targetUrl);
        restTemplate.postForEntity(targetUrl, players, ResponseDto.class);
        return null;
    }
}
