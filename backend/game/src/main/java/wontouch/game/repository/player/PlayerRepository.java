package wontouch.game.repository.player;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.domain.Player;
import wontouch.game.domain.PlayerStatus;
import wontouch.game.repository.crop.CropRedisRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class PlayerRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CropRedisRepository cropRepository;
    private static final String GAME_PREFIX = "game:";
    private static final String PLAYER_PREFIX = "player:";
    private static final String PLAYER_SUFFIX = ":player";
    private static final String PLAYER_INFIX = ":player:";
    private static final String CROP_SUFFIX = ":crop";
    private static final String INFO_SUFFIX = ":info";

    public PlayerRepository(RedisTemplate<String, Object> redisTemplate, CropRedisRepository cropRepository) {
        this.redisTemplate = redisTemplate;
        this.cropRepository = cropRepository;
    }

    // 플레이어 저장
    public void savePlayer(String roomId, Player player) {
        String playerKey = PLAYER_PREFIX + player.getId() + INFO_SUFFIX;
        redisTemplate.opsForHash().put(playerKey, "nickname", player.getNickname());
        redisTemplate.opsForHash().put(playerKey, "gold", 15000);
        redisTemplate.opsForHash().put(playerKey, "roomId", roomId);
    }

    // 플레이어 상태 세팅
    public void setPlayerStatus(String roomId, String playerId, PlayerStatus status) {
        String playerStatusKey = GAME_PREFIX + roomId + PLAYER_SUFFIX;
        redisTemplate.opsForHash().put(playerStatusKey, playerId, status.toString());
    }

    // 플레이어의 보유 작물 조회
    public Map<Object, Object> findAllCropsByPlayer(String playerId) {
        String playerCropKey = PLAYER_PREFIX + playerId + CROP_SUFFIX;
        // 모든 필드-값을 가져오기 (Map 형태로 반환)
        return redisTemplate.opsForHash().entries(playerCropKey);
    }

    public int calculateTotalGold(String roomId, String playerId) {
        int totalGold = getPlayerGold(playerId);
        Map<Object, Object> allCropsByPlayer = findAllCropsByPlayer(playerId);
        for (Object cropId : allCropsByPlayer.keySet()) {
            int cropPrice = cropRepository.getCropPrice(roomId, cropId);
            int cropQuantity = (Integer) allCropsByPlayer.get(cropId);
            totalGold += cropPrice * cropQuantity;
        }

        return totalGold;
    }

    private int getPlayerGold(String playerId) {
        String playerKey = PLAYER_PREFIX + playerId + INFO_SUFFIX;
        return (Integer) redisTemplate.opsForHash().get(playerKey, "gold");
    }
}
