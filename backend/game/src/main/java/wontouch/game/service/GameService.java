package wontouch.game.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wontouch.game.domain.Player;

import java.util.List;

@Service
public class GameService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PLAYER_PREFIX = "player:";

    public GameService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 게임이 시작될 때 플레이어들의 초기 상태를 설정하는 메서드
    public void initializeGame(String roomId, List<Player> players) {
        for (Player player : players) {
            String playerKey = PLAYER_PREFIX + player.getId();
            // 초기 설정값을 Redis의 HASH에 저장
            redisTemplate.opsForHash().put(playerKey, "gold", 100);
            redisTemplate.opsForHash().put(playerKey, "wood", 50);
            redisTemplate.opsForHash().put(playerKey, "health", 100);
            redisTemplate.opsForHash().put(playerKey, "level", 1);
        }
    }
}
