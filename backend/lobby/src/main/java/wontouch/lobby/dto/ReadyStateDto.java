package wontouch.lobby.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadyStateDto {
    private boolean isReady;
    private boolean isAllReady;
}
