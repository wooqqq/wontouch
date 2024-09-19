package wontouch.lobby.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wontouch.lobby.domain.Room;
import wontouch.lobby.dto.CreateRoomRequestDto;
import wontouch.lobby.dto.RoomResponseDto;
import wontouch.lobby.repository.RoomRepository;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomResponseDto> findRoomByPage(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() + 1; // Pageable은 0부터 시작하므로 1을 더함
        int pageSize = pageable.getPageSize();
        return roomRepository.getRooms(pageNumber, pageSize);
    }

    public void createRoom(CreateRoomRequestDto room) {
        roomRepository.saveRoom(room);
    }
}
