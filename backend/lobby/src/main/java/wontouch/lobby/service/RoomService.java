package wontouch.lobby.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.lobby.dto.*;
import wontouch.lobby.repository.RoomRepository;

import java.util.List;

@Service
public class RoomService {

    @Value("${server.url}:${socket.server.url}")
    private String socketServerUrl;
    private final RestTemplate restTemplate;
    private final RoomRepository roomRepository;

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

    public RoomResponseDto exitRoom(String roomId, long playerId) {
        return roomRepository.exitRoom(roomId, playerId);
    }

    public void addSession(SessionSaveDto sessionSaveDto) {
        roomRepository.saveSession(sessionSaveDto);
    }

    private void notifyToSocketServer(NotifyMessageDto notifyMessageDto) {
        String notifyUrl = socketServerUrl + "/notify";
        restTemplate.postForObject(notifyUrl, notifyMessageDto, void.class);

    }
}
