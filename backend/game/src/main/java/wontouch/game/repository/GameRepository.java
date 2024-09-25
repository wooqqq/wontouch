package wontouch.game.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.domain.Player;

@Repository
public class GameRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PLAYER_PREFIX = "player:";

    public GameRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void savePlayer(Player player) {
        String playerKey = PLAYER_PREFIX + player.getId();
        redisTemplate.opsForHash().put(playerKey, "nickname", player.getNickname());
        redisTemplate.opsForHash().put(playerKey, "gold", 0);
    }
}
