package wontouch.game.repository.player;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.domain.Player;
import wontouch.game.domain.PlayerStatus;

import java.util.Map;

@Repository
public class PlayerRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String GAME_PREFIX = "game:";
    private static final String PLAYER_PREFIX = "player:";
    private static final String PLAYER_SUFFIX = ":player";
    private static final String PLAYER_INFIX = ":player:";
    private static final String CROP_SUFFIX = ":crop";
    private static final String INFO_SUFFIX = ":info";

    public PlayerRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void savePlayer(String roomId, Player player) {
        String playerKey = PLAYER_PREFIX + player.getId() + INFO_SUFFIX;
        redisTemplate.opsForHash().put(playerKey, "nickname", player.getNickname());
        redisTemplate.opsForHash().put(playerKey, "gold", 0);
        redisTemplate.opsForHash().put(playerKey, "roomId", roomId);
    }

    public void setPlayerStatus(String roomId, String playerId, PlayerStatus status) {
        String playerStatusKey = GAME_PREFIX + roomId + PLAYER_SUFFIX;
        redisTemplate.opsForHash().put(playerStatusKey, playerId, status.toString());

    }

    public Map<Object, Object> findAllCropsByPlayer(String playerId) {
        String playerCropKey = PLAYER_PREFIX + playerId + CROP_SUFFIX;
        // 모든 필드-값을 가져오기 (Map 형태로 반환)

        return redisTemplate.opsForHash().entries(playerCropKey);
    }
}
