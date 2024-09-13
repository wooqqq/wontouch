package wontouch.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import wontouch.api.dto.CreateRoomRequestDto;
import wontouch.api.dto.RoomResponseDto;

import java.util.UUID;

@RestController
@RequestMapping("/lobby")
@Slf4j
public class LobbyController {

    private final RestTemplate restTemplate = new RestTemplate();

    public LobbyController() {
    }

    @GetMapping("/create/random-uuid")
    public ResponseEntity<String> createRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        return ResponseEntity.ok(uuid);
    }

    @PostMapping("/create/room")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        String lobbyServerUrl = "http://localhost:8083/api/rooms/create"; // 로비 서버 URL
        try {
            // 로비 서버로 roomId를 POST 요청으로 전송
            restTemplate.postForObject(lobbyServerUrl, createRoomRequestDto, String.class);
            log.info("Room ID sent to Lobby Server: {}", createRoomRequestDto.getRoomId());
        } catch (Exception e) {
            log.error("Failed to send Room ID to Lobby Server: {}", e.getMessage());
        }

        return null;
    }
}
