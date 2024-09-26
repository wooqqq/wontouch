package wontouch.game.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CropTransactionResult {
    private TransactionStatusType type;        // 거래 상태
    private CropTransactionResponseDto dto;    // 거래 정보
}
