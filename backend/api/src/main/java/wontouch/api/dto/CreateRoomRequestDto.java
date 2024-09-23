package wontouch.api.dto;

import lombok.Getter;

@Getter
public class CreateRoomRequestDto {
    private final String roomId;
    private String roomName;
    private final long hostPlayerId;

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
