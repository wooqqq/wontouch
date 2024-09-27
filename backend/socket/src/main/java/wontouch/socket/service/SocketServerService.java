package wontouch.socket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import wontouch.socket.dto.MessageResponseDto;
import wontouch.socket.dto.MessageType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class SocketServerService {
    // 다른 서버를 거치지 않는 소켓 서버 내의 로직들

    private static final int MAP_WIDTH = 4480;
    private static final int MAP_HEIGHT = 2560;

    private final WebSocketSessionService sessionService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, ReentrantLock> playerLocks = new ConcurrentHashMap<>();

    public SocketServerService(WebSocketSessionService sessionService) {
        this.sessionService = sessionService;
    }

    public Object moveUser(String playerId, Map<String, Object> msgMap) {
        playerLocks.putIfAbsent(playerId, new ReentrantLock());
        ReentrantLock lock = playerLocks.get(playerId);

        lock.lock();
        try {
            String newDir = (String) msgMap.get("dir");
            int newX = (Integer) msgMap.get("x");
            int newY = (Integer) msgMap.get("y");

            if (isValidMove(newX, newY)) {
                // 방에 있는 다른 플레이어들에게 브로드캐스트
                Map<String, Object> moveMessage = new HashMap<>();
                moveMessage.put("type", "MOVE");
                moveMessage.put("playerId", playerId);
                moveMessage.put("x", newX);
                moveMessage.put("y", newY);

                // 브로드캐스트 또는 좌표 업데이트 로직
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    private boolean isValidMove(int x, int y) {
        return (x >= 0 && y >= 0 && x <= MAP_WIDTH && y <= MAP_HEIGHT);
    }

//      비상용
    private void broadcastMessage(String roomId, MessageType messageType, Object content) throws IOException {
        log.debug("MessageBroadCast");
        Map<String, WebSocketSession> roomSessions = sessionService.getSessions(roomId);
        for (WebSocketSession session : roomSessions.values()) {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(new MessageResponseDto(messageType, content))));
        }
    }
}
