package wontouch.lobby.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.lobby.dto.*;
import wontouch.lobby.repository.room.RoomRepository;

import java.util.List;

@Service
public class RoomService {

    private final RestTemplate restTemplate;
    private final RoomRepository roomRepository;

    @Value("${socket.server.name}:${socket.server.path}")
    private String socketServerUrl;

    @Value("${api.server.name}:${api.server.path}")
    private String apiServerUrl;

    public RoomService(RoomRepository roomRepository) {
        this.restTemplate = new RestTemplate();
        this.roomRepository = roomRepository;
    }

    public List<RoomResponseDto> findRoomByPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() + 1; // Pageable은 0부터 시작하므로 1을 더함
        int pageSize = pageable.getPageSize();
        return roomRepository.getRooms(pageNumber, pageSize);
    }

    public RoomResponseDto createRoom(CreateRoomRequestDto room) {
        return roomRepository.saveRoom(room);
    }

    public RoomResponseDto joinRoom(String roomId, RoomRequestDto joinRequest) {
        return roomRepository.joinRoom(roomId, joinRequest);
    }

    public RoomResponseDto quickJoin(RoomRequestDto joinRequest) {
        return roomRepository.quickJoin(joinRequest);
    }

    public RoomResponseDto exitRoom(String roomId, long playerId) {
        String id = Long.toString(playerId);
        return roomRepository.exitRoom(roomId, id);
    }

    public void addSession(SessionSaveDto sessionSaveDto) {
        roomRepository.saveSession(sessionSaveDto);
    }

    public void removeSession(SessionDeleteDto sessionDeleteDto) {
        String roomId = sessionDeleteDto.getRoomId();
        String playerId = sessionDeleteDto.getPlayerId();
        roomRepository.exitRoom(roomId, playerId);
        roomRepository.removeSession(roomId, sessionDeleteDto.getSessionId());
    }

    private void notifyToSocketServer(NotifyMessageDto notifyMessageDto) {
        String notifyUrl = socketServerUrl + "/notify";
        restTemplate.postForObject(notifyUrl, notifyMessageDto, void.class);

    }

    public void inviteFriend(RoomInviteRequestDto requestDto) {
        RoomInviteResponseDto inviteDto = roomRepository.inviteFriend(requestDto);
        GameInviteRequestDto gameInviteRequestDto = GameInviteRequestDto.builder()
                .senderId(inviteDto.getSenderId())
                .receiverId(inviteDto.getReceiverId())
                .roomId(inviteDto.getRoomId())
                .roomName(inviteDto.getRoomName())
                .password(inviteDto.getPassword())
                .build();

        // api 서버 호출하여 알림 보내기
        String apiUrl = String.format("%s/notification/game-invite", apiServerUrl);

        HttpEntity<GameInviteRequestDto> requestEntity = new HttpEntity<>(gameInviteRequestDto);

        ResponseEntity<Void> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 성공적으로 알림이 전송됨
            System.out.println("Game invite notification sent successfully.");
        } else {
            // 알림 전송 실패 처리
            System.out.println("Failed to send game invite notification: " + response.getStatusCode());
        }
    }
}
