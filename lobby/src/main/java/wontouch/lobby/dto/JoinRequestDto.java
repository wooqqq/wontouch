package wontouch.lobby.dto;

import lombok.Data;

@Data
public class JoinRequestDto {
    private long playerId;
    private String roomId;
}
