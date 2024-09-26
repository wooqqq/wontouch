package wontouch.game.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.domain.Player;
import wontouch.game.entity.Crop;

import java.util.List;

@Repository
@Slf4j
public class GameRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ROOM_PREFIX = "game:room:";
    private static final String PLAYER_PREFIX = "player:";

    public GameRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void savePlayer(String roomId, Player player) {
        String playerKey = PLAYER_PREFIX + player.getId();
        redisTemplate.opsForHash().put(playerKey, "nickname", player.getNickname());
        redisTemplate.opsForHash().put(playerKey, "gold", 0);
        redisTemplate.opsForHash().put(playerKey, "roomId", roomId);
    }

    public int updateRound(String roomId) {
        String roomKey = ROOM_PREFIX + roomId;

        // Redis에서 round 값을 가져옴
        String roundValue = (String) redisTemplate.opsForHash().get(roomKey, "round");

        // null이면 0으로 시작, 아니면 Integer로 변환하여 1 증가
        int round = roundValue != null ? Integer.parseInt(roundValue) : 0;
        round += 1;

        // 갱신된 round 값을 Redis에 저장
        redisTemplate.opsForHash().put(roomKey, "round", Integer.toString(round));

        // 로그 출력
        log.debug("Updated round for room {}: {}", roomId, round);

        return round;
    }

    public void initCrops(Player player, List<Crop> crops) {
        String playersCropKey = PLAYER_PREFIX + player.getId() + ":crop";
        log.debug("playersCropKey: {}", playersCropKey);
        for (Crop crop : crops) {
            redisTemplate.opsForHash().put(playersCropKey, crop.getId(), 0);
        }
    }
}
