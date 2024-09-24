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
import wontouch.socket.dto.MessageResponseDto;
import wontouch.socket.dto.MessageType;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// 웹소켓 핸들러
// 소켓으로 보내지는 데이터는 이곳을 모두 거침
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
        String playerId = getPlayerIdFromQueryParams(session);

        session.getAttributes().put("playerId", playerId);

        roomSessions.putIfAbsent(roomId, new ConcurrentHashMap<>());
        roomSessions.get(roomId).put(session.getId(), session);

        // client로 접속되었음을 return
        session.sendMessage(new TextMessage("Room ID Created: " + roomId));

        // 입장 메세지 전송
        broadcastMessage(roomId, MessageType.NOTIFY, playerId + "이 입장하였습니다.");

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
        String playerId = (String) session.getAttributes().get("playerId");

        roomSessions.get(roomId).remove(session.getId());
        if (roomSessions.get(roomId).isEmpty()) {
            roomSessions.remove(roomId);
        }
        broadcastMessage(roomId, MessageType.NOTIFY, playerId + "이 퇴장하였습니다.");
        log.debug("Session " + session.getId() + " left room " + roomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomIdFromSession(session);
        String payload = message.getPayload();
        Map<String, Object> msgMap = WebSocketMessageParser.parseMessage(payload);
        Object content = null;
        //String messageType = (String) msgMap.get("type");
        MessageType messageType = MessageType.valueOf((String) msgMap.get("type"));
        log.debug(messageType.toString());
        switch (messageType) {
            case CHAT:
                // 채팅 메시지 브로드캐스트
                broadcastMessage(roomId, MessageType.CHAT, (String) msgMap.get("content"));
                break;
            case NOTIFY:
                // 알림 메시지 처리
                broadcastMessage(roomId, MessageType.NOTIFY, (String) msgMap.get("content"));
                break;
            case KICK:
                content = MessageHandlerFactory.handleMessage(lobbyServerUrl, roomId, messageType, msgMap);
                kickUser(roomId, (Boolean) content, (String) msgMap.get("playerId"));
                break;
            case MOVE:
                broadcastMessage(roomId, MessageType.MOVE, (String) msgMap.get("content"));
            default:
                // 기타 메시지 처리
                content = MessageHandlerFactory.handleMessage(lobbyServerUrl, roomId, messageType, msgMap);
                broadcastMessage(roomId, messageType, content);
                break;
        }
        log.info("Received message from room " + roomId + ": " + messageType);
    }

    private void broadcastMessage(String roomId, MessageType messageType, Object content) throws IOException {
        if (roomSessions.containsKey(roomId)) {
            MessageResponseDto message = new MessageResponseDto(messageType, content);
            String jsonMessage = mapper.writeValueAsString(message);
            for (WebSocketSession session : roomSessions.get(roomId).values()) {
                session.sendMessage(new TextMessage(jsonMessage));
            }
        }
    }

    public void kickUser(String roomId, boolean isKicked, String playerId) throws IOException {
        ConcurrentHashMap<String, WebSocketSession> sessions = roomSessions.get(roomId);
        if (!isKicked) return;
        if (sessions != null) {
            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
                WebSocketSession session = entry.getValue();
                String sessionPlayerId = (String) session.getAttributes().get("playerId");
                log.debug("session: {}, sessionPlayerId: {}",session.getId(), sessionPlayerId);

                if (playerId.equals(sessionPlayerId)) {
                    session.close(CloseStatus.NORMAL);  // 소켓 연결 해제
                    sessions.remove(entry.getKey());  // roomSessions에서 세션 삭제
                    broadcastMessage(roomId, MessageType.NOTIFY, playerId + " 이 방에서 강퇴되었습니다.");
                    log.debug("Player {} has been kicked from room {}", playerId, roomId);
                    break;
                }
            }
        }
    }


    private String getRoomIdFromSession(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getPath().split("/")[4];
    }

    private String getPlayerIdFromQueryParams(WebSocketSession session) {
        String query = session.getUri().getQuery();
        Map<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().
                getQueryParams().toSingleValueMap();
        return queryParams.get("playerId");
    }
}
