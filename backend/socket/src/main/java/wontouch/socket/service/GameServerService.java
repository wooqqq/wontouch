package wontouch.socket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import wontouch.socket.dto.game.CropDto;
import wontouch.socket.dto.game.CropTransactionResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

// 게임 서버로 이동하여 처리하는 로직들을 담는 서비스 레이어
@Service
@Slf4j
public class GameServerService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${game.server.name}:${game.server.path}")
    private String gameServerUrl;

    // 작물 구매 요청
    public CropTransactionResult buyCropRequest(String roomId, Map<String, Object> transactionInfo) {
        String buyCropUrl = gameServerUrl + "/town/buy/crop/" + roomId;
        log.debug("buyCropUrl: {}", buyCropUrl);
        transactionInfo.put("action", "BUY");
        CropTransactionResult result = restTemplate.postForObject(buyCropUrl, transactionInfo, CropTransactionResult.class);
        log.debug("result: {}", result);
        return result;
    }

    // 작물 판매 요청
    public CropTransactionResult sellCropRequest(String roomId, Map<String, Object> transactionInfo) {
        String sellCropUrl = gameServerUrl + "/town/sell/crop/" + roomId;
        log.debug("sellCropUrl: {}", sellCropUrl);
        transactionInfo.put("action", "SELL");
        CropTransactionResult result = restTemplate.postForObject(sellCropUrl, transactionInfo, CropTransactionResult.class);
        log.debug("result: {}", result);
        return result;
    }

    // 플레이어의 보유 작물 리스트 요청
    public Object getPlayerCrops(String playerId) {
        String playerCropsUrl = gameServerUrl + "/player/crop/list/" + playerId;
        log.debug("playerCropsUrl: {}", playerCropsUrl);
        Object playerCrops = restTemplate.getForObject(playerCropsUrl, Object.class);
        log.debug("Player's crop List: {}", playerCrops);
        return playerCrops;
    }

    // 마을의 보유 작물 리스트 요청
    public Object getTownCrops(String roomId, Map<String, Object> townInfo) {
        String townType = townInfo.get("townName").toString();
        String townCropsUrl = gameServerUrl + "/town/crop/list/" + roomId + "/" + townType;
        Object townCrops = restTemplate.getForObject(townCropsUrl, Object.class);
        return townCrops;
    }

}
