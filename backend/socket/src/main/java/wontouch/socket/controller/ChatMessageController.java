package wontouch.socket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private static final Logger log = LoggerFactory.getLogger(ChatMessageController.class);

    @MessageMapping("/chat/room")
    @SendTo("/sub/chat/room")
    public String sendMessage(String message) {
        log.debug("Received message: {}", message );
        return message;
    }


}
