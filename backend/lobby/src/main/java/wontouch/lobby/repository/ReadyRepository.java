package wontouch.lobby.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.lobby.dto.ResponseDto;

// 게임의 준비 시작을 담당하는 레포지토리 레이어
@Repository
@Slf4j
public class ReadyRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public ReadyRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean readyStatusChange(String roomId, long playerId) {
        String participantsKey = "game_lobby:" + roomId + ":participants";
        // 1. 기존 값 불러오기 (Optional)
        String field = Long.toString(playerId);
        boolean existingValue = (boolean) redisTemplate.opsForHash()
                .get(participantsKey, field);

        // 2. 필요한 경우 값을 변경하는 로직 (여기서는 새로운 값으로 덮어쓰기)
        log.info("Existing value: " + existingValue);  // 기존 값을 로그로 확인

        // 3. 변경된 값을 다시 Redis Hash에 반영
        redisTemplate.opsForHash().put(participantsKey, field, !existingValue);

        log.info("Updated value for field '" + field + "' in hash '" + participantsKey + "': " + !existingValue);

        return !existingValue;
    }
}
