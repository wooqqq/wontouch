package wontouch.lobby.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JoinRequestDto {
    private long playerId;
    private String roomId;
}
