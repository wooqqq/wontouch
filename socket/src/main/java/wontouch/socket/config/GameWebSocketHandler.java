package wontouch.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import wontouch.socket.dto.CreateRoomRequest;

import java.util.UUID;

@Slf4j
@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final RestTemplate restTemplate = new  RestTemplate();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received message: " + payload);

        session.sendMessage(new TextMessage("Server response: " + payload));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = session.getId();
        // client로 전송?
        session.sendMessage(new TextMessage("Room ID Created: " + roomId));

        log.info("Connection established: " + roomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection closed: " + session.getId());
    }
}
