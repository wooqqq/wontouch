package wontouch.api.dto;

import lombok.Getter;

@Getter
public class CreateRoomRequestDto {
<<<<<<< HEAD
    private final String roomId;
    private String roomName;
    private final long hostPlayerId;
=======
    private String roomId;
    private String roomName;
    private long hostPlayerId;
>>>>>>> 1208bf72810ddd7108d6a2a56d1a3b9e95685dc7
    private boolean isPrivate;
    private String password;

    public CreateRoomRequestDto(String roomId, String roomName,
                                long hostPlayerId, boolean isPrivate,
                                String password) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.hostPlayerId = hostPlayerId;
        this.isPrivate = isPrivate;
        this.password = password;
    }
}
