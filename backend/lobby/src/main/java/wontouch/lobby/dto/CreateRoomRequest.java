package wontouch.lobby.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateRoomRequest {
    private String roomId;
    private String roomName;
    private long hostPlayerId;
    private boolean isPrivate;
    private String password;
}
