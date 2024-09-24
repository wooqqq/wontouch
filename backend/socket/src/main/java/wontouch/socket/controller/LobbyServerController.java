package wontouch.socket.controller;

import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class LobbyServerController {
    private final SimpMessagingTemplate messagingTemplate;

    public LobbyServerController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 준비 상태 메시지를 처리
    @MessageMapping("/game/ready")
    public void handleReadyMessage(Map<String, Object> message) {
        // 로비 서버나 다른 플레이어에게 준비 상태 알림 전송
        messagingTemplate.convertAndSend("/lobby/status/ready", message);
    }
}
