package wontouch.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, ConcurrentHashMap<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomIdFromSession(session);
        roomSessions.putIfAbsent(roomId, new ConcurrentHashMap<>());
        roomSessions.get(roomId).put(session.getId(), session);

        // client로 전송?
        session.sendMessage(new TextMessage("Room ID Created: " + roomId));

        log.debug("Session " + session.getId() + " joined room " + roomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getRoomIdFromSession(session);
        roomSessions.get(roomId).remove(session.getId());
        if (roomSessions.get(roomId).isEmpty()) {
            roomSessions.remove(roomId);
        }
        log.debug("Session " + session.getId() + " left room " + roomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomIdFromSession(session);
        String payload = message.getPayload();
        log.info("Received message from room " + roomId + ": " + payload);

        broadcastMessage(roomId, payload);
    }

    private void broadcastMessage(String roomId, String message) throws IOException {
        if (roomSessions.containsKey(roomId)) {
            for (WebSocketSession session : roomSessions.get(roomId).values()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
    private String getRoomIdFromSession(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath().split("/")[3];
    }
}
