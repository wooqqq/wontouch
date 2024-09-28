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
    private static final int TOTAL_EXPERIENCE = 1000;
    private static final int TOTAL_MILEAGE = 500;
    private static final int MAX_PLAYER = 8;

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
    public Map<String, Map<String, Integer>> getTotalGold(String roomId) {
        String playerStatusKey = GAME_PREFIX + roomId + PLAYER_SUFFIX;
        Set<Object> playerIds = redisTemplate.opsForHash().keys(playerStatusKey);
        Map<String, Integer> totalGoldResponse = new HashMap<>();
        int totalGoldSum = 0;

        // 총 인원이 8명으로 설정되어 있다고 가정
        int actualPlayers = playerIds.size();  // 실제 참가 인원 수

        // 실제 참가 인원이 총 인원보다 적을 경우, 경험치와 마일리지 비례 조정
        int adjustedTotalExperience = TOTAL_EXPERIENCE * actualPlayers / MAX_PLAYER;
        int adjustedTotalMileage = TOTAL_MILEAGE * actualPlayers / MAX_PLAYER;

        // 보상 결과를 저장할 Map
        Map<String, Map<String, Integer>> rewardDistribution = new HashMap<>();

        // 플레이어마다 골드 계산
        for (Object playerId : playerIds) {
            int totalGold = playerRepository.calculateTotalGold(roomId, (String) playerId);
            totalGoldSum += totalGold;
            totalGoldResponse.put(playerId.toString(), totalGold);
        }

        for (Object playerId : playerIds) {
            int totalGold = totalGoldResponse.get(playerId.toString());

            // 플레이어가 차지하는 골드 비율 계산
            double goldRatio = (double) totalGold / totalGoldSum;

            // 비율에 따라 경험치와 마일리지 계산
            int experienceEarned = (int) (adjustedTotalExperience * goldRatio);
            int mileageEarned = (int) (adjustedTotalMileage * goldRatio);

            // 플레이어에게 보상 분배
            Map<String, Integer> playerRewards = new HashMap<>();
            playerRewards.put("totalGold", totalGold);
            playerRewards.put("experience", experienceEarned);
            playerRewards.put("mileage", mileageEarned);
            rewardDistribution.put(playerId.toString(), playerRewards);
        }
        return rewardDistribution;
    }
}
