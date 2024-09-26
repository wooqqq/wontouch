package wontouch.socket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.socket.dto.ReadyStateDto;

import java.util.Map;
import java.util.Objects;

// 게임 서버로 이동하여 처리하는 로직들을 담는 서비스 레이어
@Service
@Slf4j
public class GameServerService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void buyCropRequest(String gameServerUrl, String roomId, Map<String, Object> transactionInfo) {
        String buyCropUrl = gameServerUrl + "/buy/rop/" + roomId;
        log.debug("buyCropUrl: {}", buyCropUrl);
        Objects state = restTemplate.postForObject(buyCropUrl, transactionInfo, Objects.class);
        log.debug("result: {}", state);
    }
}
