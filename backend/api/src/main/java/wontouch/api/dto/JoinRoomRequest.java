package wontouch.api.dto;

import lombok.Data;

@Data
public class JoinRoomRequest {
    private long playerId;
    private String roomId;
}
