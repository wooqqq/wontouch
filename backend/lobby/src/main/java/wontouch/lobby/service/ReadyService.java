package wontouch.lobby.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wontouch.lobby.dto.ReadyStateDto;
import wontouch.lobby.repository.ReadyRepository;

import java.util.Map;

// 게임의 준비 시작을 담당하는 서비스 레이어
@Service
public class ReadyService {

    private final ReadyRepository readyRepository;

    @Autowired
    public ReadyService(ReadyRepository readyRepository) {
        this.readyRepository = readyRepository;
    }

    public ReadyStateDto updateReadyState(Map<String, Object> preparationInfo) {
        String roomId = preparationInfo.get("roomId").toString();
        long playerId = Long.parseLong(preparationInfo.get("playerId").toString());
        return readyRepository.readyStatusChange(roomId, playerId);
    }

    public void kickUser(Map<String, Object> kickInfo) {
        String roomId = kickInfo.get("roomId").toString();
        String requestUserId = kickInfo.get("requestUserId").toString();
        String playerId = kickInfo.get("playerId").toString();
        readyRepository.kickUser(requestUserId, roomId, playerId);
    }
}
