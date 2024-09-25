package wontouch.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import wontouch.socket.service.WebSocketSessionService;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketSessionService sessionService;

    public WebSocketConfig(WebSocketSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Bean
    public GameWebSocketHandler gameWebSocketHandler() {
        log.info("game handler register");
        return
                new GameWebSocketHandler(sessionService); // Bean으로 직접 등록
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler(), "ws/game/{roomId}").setAllowedOrigins("*");
    }
}
