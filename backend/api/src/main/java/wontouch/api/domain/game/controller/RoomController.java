package wontouch.api.domain.game.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import wontouch.api.domain.game.dto.request.PlayerRequestDto;
import wontouch.api.domain.game.dto.request.CreateRoomRequestDto;
import wontouch.api.domain.game.dto.request.RoomRequestDto;
import wontouch.api.domain.game.model.service.RoomService;
import wontouch.api.domain.notification.dto.request.GameInviteRequestDto;
import wontouch.api.domain.notification.model.service.NotificationService;
import wontouch.api.global.exception.CustomException;
import wontouch.api.global.exception.ExceptionResponse;
import wontouch.api.global.dto.ResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final RoomService roomService;
    private final NotificationService notificationService;

    // 로비서버 기본 주소
    @Value("${lobby.server.name}:${lobby.server.path}")
    private String lobbyServerUrl;

    // 게임서버 기본 주소
    @Value("${game.server.name}:${game.server.path}")
    private String gameServerUrl;

    @GetMapping("/create/random-uuid")
    public ResponseEntity<ResponseDto<?>> createRandomUUID() {
        log.debug("CREATE RANDOM UUID");
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
        if (createRoomRequestDto.getPassword() != null && !createRoomRequestDto.getPassword().isEmpty()) {
            createRoomRequestDto.setSecret(true);
        }
        log.debug("create Room: {}", createRoomRequestDto);
        ResponseDto responseDto = null;
        String targetUrl = lobbyServerUrl + "/rooms/create"; // 로비 서버 URL
        try {
            // 로비 서버로 roomId를 POST 요청으로 전송
            responseDto = restTemplate.postForObject(targetUrl, createRoomRequestDto, ResponseDto.class);
            log.info("Room ID sent to Lobby Server: {}", createRoomRequestDto.getRoomId());
        } catch (HttpClientErrorException.Conflict e) {
            throw new ExceptionResponse(CustomException.ALREADY_EXIST_REQUEST_EXCEPTION);
        } catch (Exception e) {
            log.error("Failed to send Room ID to Lobby Server: {}", e.getMessage());
            throw new ExceptionResponse(CustomException.UNHANDLED_ERROR_EXCEPTION);
        }

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 특정 게임방 입장
    @PostMapping("/join/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody RoomRequestDto roomRequestDto) {
        String url = String.format("%s/rooms/join/%s", lobbyServerUrl, roomId);
        log.debug("roomRequestDto: {}", roomRequestDto);
        try {
            ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, roomRequestDto, ResponseDto.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException.Unauthorized e) {
            e.printStackTrace();
            throw new ExceptionResponse(CustomException.INVALID_PASSWORD_EXCEPTION);
        } catch (HttpClientErrorException.Conflict exception) {
            exception.printStackTrace();
            throw new ExceptionResponse(CustomException.NO_AVAILABLE_ROOM_EXCEPTION);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ExceptionResponse(CustomException.ROOM_NOT_FOUND);
        }
    }

    // 게임방 빠른 입장
    @PostMapping("/quick-join")
    public ResponseEntity<?> quickJoin(@RequestBody RoomRequestDto roomRequestDto) {
        String url = String.format("%s/rooms/quick-join", lobbyServerUrl);
        log.debug("roomRequestDto: {}", roomRequestDto);
        try {
            ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, roomRequestDto, ResponseDto.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpClientErrorException.Unauthorized e) {
//          일단 뭉뚱그린 예외 처리
            e.printStackTrace();
            throw new ExceptionResponse(CustomException.INVALID_PASSWORD_EXCEPTION);
        } catch (HttpClientErrorException.Conflict exception) {
            exception.printStackTrace();
            throw new ExceptionResponse(CustomException.NO_AVAILABLE_ROOM_EXCEPTION);
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
            throw new ExceptionResponse(CustomException.UNHANDLED_ERROR_EXCEPTION);
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
                new ParameterizedTypeReference<ResponseDto<?>>() {
                }
        );
        return response;
    }

    @PostMapping("/start/{roomId}")
    public ResponseEntity<?> initializeGamePlayer(@PathVariable String roomId, @RequestBody List<PlayerRequestDto> players) {
        String targetUrl = String.format("%s/game/init/%s", gameServerUrl, roomId);
        String lobbyUrl = String.format("%s/ready/start/%s", lobbyServerUrl, roomId);
        log.debug("targetUrl:{}", targetUrl);
        restTemplate.postForObject(lobbyUrl, null, Object.class);
        restTemplate.postForEntity(targetUrl, players, Object.class);
        return null;
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteFriend(@RequestBody GameInviteRequestDto requestDto) {
        GameInviteRequestDto inviteRequestDto = roomService.inviteFriend(requestDto);
        notificationService.notifyGameInvite(inviteRequestDto);

        ResponseDto<String> responseDto = ResponseDto.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("게임방 초대 성공")
                .data(null)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
