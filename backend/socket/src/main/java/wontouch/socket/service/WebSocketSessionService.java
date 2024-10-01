package wontouch.socket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import wontouch.socket.dto.MessageResponseDto;
import wontouch.socket.dto.MessageType;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketSessionService {
    // 소켓 세션과 메세지 전송을 전담하는 서비스 레이어
    private final Map<String, ConcurrentHashMap<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    public void addSession(String roomId, WebSocketSession session) {
        roomSessions.putIfAbsent(roomId, new ConcurrentHashMap<>());
        roomSessions.get(roomId).put(session.getId(), session);
    }

    public void removeSession(String roomId, WebSocketSession session) {
        if (roomSessions.containsKey(roomId)) {
            roomSessions.get(roomId).remove(session.getId());
            if (roomSessions.get(roomId).isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

    public Map<String, WebSocketSession> getSessions(String roomId) {
        return roomSessions.getOrDefault(roomId, new ConcurrentHashMap<>());
    }

    public boolean roomExists(String roomId) {
        return roomSessions.containsKey(roomId);
    }

    @Async
    public synchronized void sendMessageToSession(WebSocketSession session, MessageType messageType, Object content) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(new MessageResponseDto(messageType, content))));
        }
    }

    @Async
    public synchronized void broadcastMessage(String roomId, MessageType messageType, Object content) throws IOException {
        log.debug("MessageBroadCast");
        Map<String, WebSocketSession> sessions = getSessions(roomId);
        for (WebSocketSession session : sessions.values()) {
            sendMessageToSession(session, messageType, content);
        }
    }

    // 메세지 유니캐스트
    @Async
    public void unicastMessage(String roomId, String playerId, MessageType messageType, Object content) throws IOException {
        log.debug("MessageUnicast");
        Map<String, WebSocketSession> roomSessions = getSessions(roomId);
        for (WebSocketSession session : roomSessions.values()) {
            // 세션에 저장된 플레이어 ID와 비교하여 특정 플레이어에게만 전송
            String sessionPlayerId = (String) session.getAttributes().get("playerId");

            if (playerId.equals(sessionPlayerId)) {
                // 해당 플레이어에게만 메시지를 전송

                session.sendMessage(new TextMessage(mapper.writeValueAsString(new MessageResponseDto(messageType, content))));
                log.debug("Message sent to playerId {} in room {}", playerId, roomId);
                break;  // 특정 플레이어에게만 전송 후 루프 종료
            }
        }
    }
}
