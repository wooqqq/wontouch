package wontouch.socket.config;

import wontouch.socket.dto.MessageType;
import wontouch.socket.service.GameServerService;
import wontouch.socket.service.LobbyServerService;

import java.util.Map;

public class MessageHandlerFactory {

    private static final LobbyServerService lobbyServerService = new LobbyServerService();
    private static final GameServerService gameServerService = new GameServerService();

    public static Object handleMessage(String lobbyServerUrl, String gameServerUrl, String roomId,
                                       MessageType messageType, Map<String, Object> msgMap) {
        switch (messageType) {
            case READY:
                // 로비 서버로 준비 정보 전송
                return lobbyServerService.sendPreparationInfo(lobbyServerUrl, roomId, msgMap);
            case START:
                // TODO 시작하는 로직 구현
                System.out.println("LET's START: " + msgMap);
                break;
            case KICK:
                // 유저 강퇴
                return lobbyServerService.kickUser(lobbyServerUrl, roomId, msgMap);
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
