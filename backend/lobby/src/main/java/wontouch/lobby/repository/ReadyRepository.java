package wontouch.lobby.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.lobby.dto.ReadyStateDto;

import java.util.Map;

// 게임의 준비 시작을 담당하는 레포지토리 레이어
@Repository
@Slf4j
public class ReadyRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public ReadyRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public ReadyStateDto readyStatusChange(String roomId, long playerId) {
        String participantsKey = "game_lobby:" + roomId + ":participants";
        // 1. 기존 값 불러오기
        String field = Long.toString(playerId);
        boolean isReady = (boolean) redisTemplate.opsForHash()
                .get(participantsKey, field);

        // 2. 필요한 경우 값을 변경하는 로직 (여기서는 새로운 값으로 덮어쓰기)
        log.info("Existing value: " + isReady);  // 기존 값을 로그로 확인

        // 3. 변경된 값을 다시 Redis Hash에 반영
        redisTemplate.opsForHash().put(participantsKey, field, !isReady);

        log.info("Updated value for field '" + field + "' in hash '" + participantsKey + "': " + !isReady);

        boolean isAllReady = isAllReady(roomId);
        return new ReadyStateDto(!isReady, isAllReady);
    }

    public boolean isAllReady(String roomId) {
        String participantsKey = "game_lobby:" + roomId + ":participants";

        // Hash에서 참여자 목록 가져오기
        Map<Object, Object> participantsMap = redisTemplate.opsForHash().entries(participantsKey);

        // 참가자가 없는 경우 처리
        if (participantsMap == null || participantsMap.isEmpty()) {
            // 참가자가 없으면 모두 준비되지 않은 것으로 반환
            return false;
        }

        // 모든 참가자의 readyState가 true인지 확인
        return participantsMap.entrySet().stream()
                .allMatch(entry -> {
                    Object value = entry.getValue();
                    return Boolean.parseBoolean(value.toString());
                });
    }

    public void kickUser(String roomId, String requestId, String playerId) {
        String participantsKey = "game_lobby:" + roomId + ":participants";
        String infoKey = "game_lobby:" + roomId + ":info";

        String hostId = (String) redisTemplate.opsForHash().get(infoKey, "hostId");
        log.debug("playerId:{}, hostId: {}", playerId, hostId);
        if (requestId.equals(hostId)) {
            redisTemplate.opsForHash().delete(participantsKey, playerId);
        }
    }
}
