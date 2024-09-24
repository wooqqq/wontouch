package wontouch.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// STOMP 이벤트 핸들러
@Component
@Slf4j
public class StompEventListener {

    // STOMP 연결 성공 이벤트 핸들러
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("STOMP connection established. Session ID: {}", headerAccessor.getSessionId());
    }

    // STOMP 연결 끊김 이벤트 핸들러
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("STOMP connection closed. Session ID: {}", headerAccessor.getSessionId());
    }
}