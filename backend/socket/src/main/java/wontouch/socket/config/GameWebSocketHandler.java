package wontouch.socket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;
import wontouch.socket.dto.MessageDto;
import wontouch.socket.dto.MessageType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Value("${server.url}:${lobby.server.url}")
    private String lobbyServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, ConcurrentHashMap<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomIdFromSession(session);
        String playerName = getPlayerNameFromQueryParams(session);

        session.getAttributes().put("playerName", playerName);

        roomSessions.putIfAbsent(roomId, new ConcurrentHashMap<>());
        roomSessions.get(roomId).put(session.getId(), session);

        // client로 접속되었음을 return
        session.sendMessage(new TextMessage("Room ID Created: " + roomId));

        // 입장 메세지 전송
        broadcastMessage(roomId, MessageType.NOTIFY, playerName + "이 입장하였습니다.");

        // session 정보를 로비 서버로 전송
        String sessionUrl = lobbyServerUrl + "/api/session/save";
        Map<String, Object> sessionInfo = new ConcurrentHashMap<>();
        sessionInfo.put("roomId", roomId);
        sessionInfo.put("sessionId", session.getId());
        restTemplate.postForObject(sessionUrl, sessionInfo, String.class);

        log.debug("Session " + session.getId() + " joined room " + roomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getRoomIdFromSession(session);
        String playerName = (String) session.getAttributes().get("playerName");

        roomSessions.get(roomId).remove(session.getId());
        if (roomSessions.get(roomId).isEmpty()) {
            roomSessions.remove(roomId);
        }
        broadcastMessage(roomId, MessageType.NOTIFY, playerName + "이 퇴장하였습니다.");
        log.debug("Session " + session.getId() + " left room " + roomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomIdFromSession(session);
        String payload = message.getPayload();
        Map<String, Object> msgMap = WebSocketMessageParser.parseMessage(payload);

        String messageType = (String) msgMap.get("type");
        if (messageType.equals("CHAT")) {
            broadcastMessage(roomId, MessageType.CHAT, payload);
        } else {
            MessageHandlerFactory.handleMessage(messageType, msgMap);
        }
        log.info("Received message from room " + roomId + ": " + messageType);
    }

    private void broadcastMessage(String roomId, MessageType messageType, String content) throws IOException {
        if (roomSessions.containsKey(roomId)) {
            MessageDto message = new MessageDto(messageType, content);
            String jsonMessage = mapper.writeValueAsString(message);
            for (WebSocketSession session : roomSessions.get(roomId).values()) {
                session.sendMessage(new TextMessage(jsonMessage));
            }
        }

    }

    private String getRoomIdFromSession(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath().split("/")[3];
    }

    private String getPlayerNameFromQueryParams(WebSocketSession session) {
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().
                getQueryParams().toSingleValueMap();
        return queryParams.get("playerName");
    }
}
