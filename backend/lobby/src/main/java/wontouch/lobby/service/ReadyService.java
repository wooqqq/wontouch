package wontouch.lobby.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public boolean updateReadyState(Map<String, Object> preparationInfo) {
        String roomId = preparationInfo.get("roomId").toString();
        long playerId = Long.parseLong(preparationInfo.get("playerId").toString());
        return readyRepository.readyStatusChange(roomId, playerId);
    }
}
