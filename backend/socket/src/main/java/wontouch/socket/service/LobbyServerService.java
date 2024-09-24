package wontouch.socket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LobbyServerService {

    private static final Logger log = LoggerFactory.getLogger(LobbyServerService.class);

    @Value("${server.url}:${lobby.server.url}")
    private String lobbyServerUrl;


    private final RestTemplate restTemplate = new RestTemplate();

    public void sendPreparationInfo(Map<String, Object> preparationInfo) {
        String readyUrl = "http://localhost:8083/lobby" + "/api/ready/change";
        log.debug("readyUrl:{}", readyUrl);
        restTemplate.postForObject(readyUrl, preparationInfo, String.class);
        System.out.println("Sending preparation info to Lobby Server: " + preparationInfo);
    }
}
