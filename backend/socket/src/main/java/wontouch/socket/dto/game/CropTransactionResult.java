package wontouch.socket.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CropTransactionResult {
    private TransactionStatusType type;        // 거래 상태
    private CropTransactionDto info;    // 거래 정보
}
