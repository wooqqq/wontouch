package wontouch.socket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionService {
    private final Map<String, ConcurrentHashMap<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

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
}
