package wontouch.socket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.socket.dto.ReadyStateDto;
import wontouch.socket.dto.game.CropTransactionResult;

import java.util.Map;
import java.util.Objects;

// 게임 서버로 이동하여 처리하는 로직들을 담는 서비스 레이어
@Service
@Slf4j
public class GameServerService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${game.server.name}:${game.server.path}")
    private String gameServerUrl;

    public CropTransactionResult buyCropRequest(String roomId, Map<String, Object> transactionInfo) {
        String buyCropUrl = gameServerUrl + "/shop/buy/crop/" + roomId;
        log.debug("buyCropUrl: {}", buyCropUrl);
        CropTransactionResult result = restTemplate.postForObject(buyCropUrl, transactionInfo, CropTransactionResult.class);
        log.debug("result: {}", result);
        return result;
    }
}
