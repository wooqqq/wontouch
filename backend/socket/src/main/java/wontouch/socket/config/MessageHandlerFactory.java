package wontouch.socket.config;

import org.springframework.stereotype.Component;
import wontouch.socket.dto.MessageType;
import wontouch.socket.service.GameServerService;
import wontouch.socket.service.LobbyServerService;

import java.util.Map;

@Component
public class MessageHandlerFactory {

    private final LobbyServerService lobbyServerService;
    private final GameServerService gameServerService;

    public MessageHandlerFactory(LobbyServerService lobbyServerService, GameServerService gameServerService) {
        this.lobbyServerService = lobbyServerService;
        this.gameServerService = gameServerService;
    }

    public Object handleMessage(String roomId,
                                       MessageType messageType, Map<String, Object> msgMap) {
        switch (messageType) {
            case READY:
                // 로비 서버로 준비 정보 전송
                return lobbyServerService.sendPreparationInfo(roomId, msgMap);
            case START:
                // TODO 시작하는 로직 구현
                System.out.println("LET's START: " + msgMap);
                break;
            case KICK:
                // 유저 강퇴
                return lobbyServerService.kickUser(roomId, msgMap);
            case BUY:

                System.out.println();
                return null;
            default:
                System.out.println("Unknown message type: " + messageType);
                return null;
        }
        return null;
    }
}
