package wontouch.socket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import wontouch.socket.dto.MessageType;
import wontouch.socket.service.WebSocketSessionService;

import java.io.IOException;
import java.util.Map;

@Controller()
@RequestMapping("/game")
@Slf4j
public class GameServerController {

    private final WebSocketSessionService sessionService;
    private final ObjectMapper mapper = new ObjectMapper();

    public GameServerController(WebSocketSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/round-start")
    public ResponseEntity<?> roundStart(@RequestBody Map<String, Object> messageData) {
        String roomId = (String) messageData.get("roomId");
        log.debug("START TIMER!!: {}", messageData);
        try {
            sessionService.broadcastMessage(roomId, MessageType.NOTIFY, "Timer started successfully");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Timer started successfully");
    }

    @PostMapping("/round-end")
    public ResponseEntity<?> roundEnd(@RequestBody Map<String, Object> messageData) {
        String roomId = (String) messageData.get("roomId");
        log.debug("END TIMER!!: {}", messageData);
        try {
            sessionService.broadcastMessage(roomId, MessageType.NOTIFY, "Timer ended successfully");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Timer ended successfully");
    }

    @PostMapping("/preparation-start")
    public ResponseEntity<?> preparationStart(@RequestBody Map<String, Object> messageData) {
        String roomId = (String) messageData.get("roomId");
        log.debug("START PREPARATION!!: {}", messageData);
        try {
            sessionService.broadcastMessage(roomId, MessageType.NOTIFY, "Preparation Start successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("preparation started successfully");
    }

    @PostMapping("/crop-list")
    public ResponseEntity<?> cropList(@RequestBody Map<String, Object> messageData) {
        String roomId = (String) messageData.get("roomId");
        log.debug("CROP LIST!!: {}", messageData);
        try {
            sessionService.broadcastMessage(roomId, MessageType.NOTIFY, messageData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Timer ended successfully");
    }
}
