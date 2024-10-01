package wontouch.game.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UtilService {

    private final RedisTemplate<String, Object> redisTemplate;

    public UtilService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void showMeTheMoney(String playerId) {
        String playerKey = "player:" + playerId;
        redisTemplate.opsForHash().increment(playerKey, "gold", 10000); // 골드 차감
        // gold 값을 Long으로 변환하여 가져오기
        Object goldObject = redisTemplate.opsForHash().get(playerKey, "gold");

        // 캐스팅 문제를 방지하기 위해 Long 타입으로 변환 처리
        long gold;
        if (goldObject instanceof Integer) {
            gold = ((Integer) goldObject).longValue();  // Integer를 Long으로 변환
        } else if (goldObject instanceof Long) {
            gold = (Long) goldObject;
        } else {
            throw new ClassCastException("Gold is neither Integer nor Long.");
        }
        log.debug("Current Gold: " + gold);
    }
}
