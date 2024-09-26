package wontouch.game.repository.player;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.domain.Player;

import java.util.Map;

@Repository
public class PlayerRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ROOM_PREFIX = "game:room:";
    private static final String PLAYER_PREFIX = "player:";
    private static final String CROP_POSTFIX = ":crop";

    public PlayerRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void savePlayer(String roomId, Player player) {
        String playerKey = PLAYER_PREFIX + player.getId();
        redisTemplate.opsForHash().put(playerKey, "nickname", player.getNickname());
        redisTemplate.opsForHash().put(playerKey, "gold", 0);
        redisTemplate.opsForHash().put(playerKey, "roomId", roomId);
    }

    public Map<Object, Object> findAllCropsByPlayer(String playerId) {
        String playerCropKey = PLAYER_PREFIX + playerId + CROP_POSTFIX;
        // 모든 필드-값을 가져오기 (Map 형태로 반환)

        return redisTemplate.opsForHash().entries(playerCropKey);
    }
}
