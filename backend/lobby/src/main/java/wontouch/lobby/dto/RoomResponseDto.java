package wontouch.lobby.dto;

import lombok.Getter;
import wontouch.lobby.domain.Room;

import java.util.Set;

@Getter
public class RoomResponseDto {
    private String roomId;
    private String roomName;
    private boolean isPrivate;
    private Set<String> participants;
    private int maxPlayers;
    private int currentPlayersCount;

    public RoomResponseDto(Room room) {
        this.roomId = room.getRoomId();
        this.roomName = room.getRoomName();
        this.isPrivate = room.isPrivate();
        this.participants = room.getParticipants();
        this.participants.addAll(room.getParticipants());
        this.maxPlayers = 8;
        this.currentPlayersCount = room.getCurrentPlayersCount();
    }
}
