package wontouch.socket.config;

import wontouch.socket.service.LobbyServerService;

import java.util.Map;

public class MessageHandlerFactory {

    private static final LobbyServerService lobbyServerService = new LobbyServerService();

    public static void handleMessage(String messageType, Map<String, Object> msgMap) {
        switch (messageType) {
            case "READY":
                // 로비 서버로 준비 정보 전송
                lobbyServerService.sendPreparationInfo(msgMap);
                break;
            case "START":
                // 채팅 메시지 처리 로직, 필요 시 다른 서버로 전송
                System.out.println("Broadcasting chat message: " + msgMap);
                break;
            default:
                System.out.println("Unknown message type: " + messageType);
        }
    }
}
