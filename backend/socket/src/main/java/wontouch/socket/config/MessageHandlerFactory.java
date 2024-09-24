package wontouch.socket.config;

import wontouch.socket.dto.MessageType;
import wontouch.socket.service.LobbyServerService;

import java.util.Map;

public class MessageHandlerFactory {

    private static final LobbyServerService lobbyServerService = new LobbyServerService();

    public static Object handleMessage(String lobbyServerUrl, String roomId,
                                       MessageType messageType, Map<String, Object> msgMap) {
        switch (messageType) {
            case READY:
                // 로비 서버로 준비 정보 전송
                return lobbyServerService.sendPreparationInfo(lobbyServerUrl, roomId, msgMap);
            case START:
                // 채팅 메시지 처리 로직, 필요 시 다른 서버로 전송
                System.out.println("Broadcasting chat message: " + msgMap);
                break;
            case KICK:
                // 유저 강퇴
                lobbyServerService.kickUser(lobbyServerUrl, roomId, msgMap);
                break;
            default:
                System.out.println("Unknown message type: " + messageType);
                return null;
        }
        return null;
    }
}
