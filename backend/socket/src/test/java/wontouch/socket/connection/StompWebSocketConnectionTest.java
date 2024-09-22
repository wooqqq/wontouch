package wontouch.socket.connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest()
public class StompWebSocketConnectionTest {

    @LocalServerPort // 실제 사용 중인 포트를 주입
    private int port;

    private WebSocketStompClient stompClient;
    private CompletableFuture<StompSession> stompSessionCompletableFuture;

    private final String WEBSOCKET_ENDPOINT = "/ws";

    @BeforeEach
    public void setup() throws Exception {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());

        System.out.println("PORT: " + port);
        stompSessionCompletableFuture = stompClient.connectAsync(
                "ws://localhost:" + port + WEBSOCKET_ENDPOINT,  // 동적으로 할당된 포트를 사용
                new StompSessionHandlerAdapter() {
                }
        );
    }

    @Test
    public void testStompWebSocketConnection() throws Exception {
        StompSession stompSession = stompSessionCompletableFuture.get(10, TimeUnit.SECONDS);

        assertThat(stompSession.isConnected()).isTrue();
    }
}
