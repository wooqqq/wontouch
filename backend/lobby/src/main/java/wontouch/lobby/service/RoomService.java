package wontouch.lobby.service;

<<<<<<< HEAD
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wontouch.lobby.dto.CreateRoomRequestDto;
import wontouch.lobby.dto.JoinRequestDto;
import wontouch.lobby.dto.RoomResponseDto;
import wontouch.lobby.repository.RoomRepository;

import java.util.List;

=======
import org.springframework.stereotype.Service;
import wontouch.lobby.domain.Room;
import wontouch.lobby.dto.CreateRoomRequest;
import wontouch.lobby.repository.RoomRepository;

>>>>>>> 1208bf72810ddd7108d6a2a56d1a3b9e95685dc7
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

<<<<<<< HEAD
    public List<RoomResponseDto> findRoomByPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() + 1; // Pageable은 0부터 시작하므로 1을 더함
        int pageSize = pageable.getPageSize();
        return roomRepository.getRooms(pageNumber, pageSize);
    }

    public RoomResponseDto createRoom(CreateRoomRequestDto room) {
        return roomRepository.saveRoom(room);
    }

    public RoomResponseDto joinRoom(String roomId, JoinRequestDto joinRequest) {
        return roomRepository.joinRoom(roomId, joinRequest);
=======
    public void createRoom(CreateRoomRequest room) {
        roomRepository.saveRoom(room);
>>>>>>> 1208bf72810ddd7108d6a2a56d1a3b9e95685dc7
    }
}
