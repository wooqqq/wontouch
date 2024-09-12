package wontouch.lobby.service;

import org.springframework.stereotype.Service;
import wontouch.lobby.domain.Room;
import wontouch.lobby.dto.CreateRoomRequest;
import wontouch.lobby.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void createRoom(CreateRoomRequest room) {
        roomRepository.saveRoom(room);
    }
}
