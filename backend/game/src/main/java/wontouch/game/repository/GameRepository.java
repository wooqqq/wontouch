package wontouch.game.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.domain.Player;
import wontouch.game.entity.Crop;
import wontouch.game.repository.player.PlayerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@Slf4j
public class GameRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PlayerRepository playerRepository;
    private static final String GAME_PREFIX = "game:";
    private static final String ROOM_SUFFIX = ":room";
    private static final String PLAYER_PREFIX = "player:";
    private static final String PLAYER_SUFFIX = ":player";

    public GameRepository(RedisTemplate<String, Object> redisTemplate, PlayerRepository playerRepository) {
        this.redisTemplate = redisTemplate;
        this.playerRepository = playerRepository;
    }

    public int updateRound(String roomId) {
        String roomKey = GAME_PREFIX + roomId + ROOM_SUFFIX;

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

    // 최종 골드 계산
    public Map<String, Integer> getTotalGold(String roomId) {
        String playerStatusKey = GAME_PREFIX + roomId + PLAYER_SUFFIX;
        Set<Object> playerIds = redisTemplate.opsForHash().keys(playerStatusKey);
        Map<String, Integer> totalGoldResponse = new HashMap<>();

        // 플레이어마다 골드 계산
        for (Object playerId : playerIds) {
            int totalGold = playerRepository.calculateTotalGold(roomId, (String) playerId);
            totalGoldResponse.put(playerId.toString(), totalGold);
        }
        return totalGoldResponse;
    }
}
