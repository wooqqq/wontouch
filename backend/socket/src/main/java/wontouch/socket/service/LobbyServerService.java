package wontouch.socket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.socket.config.GameWebSocketHandler;
import wontouch.socket.dto.MessageType;
import wontouch.socket.dto.ReadyStateDto;

import java.io.IOException;
import java.util.Map;

@Service
public class LobbyServerService {

    private static final Logger log = LoggerFactory.getLogger(LobbyServerService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final GameWebSocketHandler gameWebSocketHandler = new GameWebSocketHandler();


    public ReadyStateDto sendPreparationInfo(String lobbyServerUrl, String roomId,
                                             Map<String, Object> preparationInfo) {
        String readyUrl = lobbyServerUrl + "/ready/toggle";
        log.debug("readyUrl:{}", readyUrl);
        preparationInfo.put("roomId", roomId);
        System.out.println("Sending preparation info to Lobby Server: " + preparationInfo);
        ReadyStateDto state = restTemplate.postForObject(readyUrl, preparationInfo, ReadyStateDto.class);
        return state;
    }

    public void kickUser(String lobbyServerUrl, String roomId,
                         Map<String, Object> msgMap) {
        log.debug("kickInfo:{}", msgMap);

    }
}
