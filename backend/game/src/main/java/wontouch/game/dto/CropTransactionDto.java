package wontouch.game.dto;

import lombok.Data;
import wontouch.game.domain.Action;

@Data
public class CropTransactionDto {
    private String cropId;
    private String cropName;
    private int quantity;
    private Action action;
    private long playerId;
}
