package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.game.domain.PlayerStatus;
import wontouch.game.repository.GameRepository;
import wontouch.game.repository.player.PlayerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class TimerService {
    private static final int ROUND_DURATION_SECONDS = 30;
    private static final int PREPARATION_DURATION_SECONDS = 10;
    private static final int FINAL_ROUND = 5;

    @Value("${socket.server.name}:${socket.server.path}")
    private String socketServerUrl;
    private static final String GAME_PREFIX = "game:";
    private static final String PLAYER_SUFFIX = ":player";

    // 라운드를 관리하는 타이머
    private Map<String, ScheduledFuture<?>> roundTimers = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate redisTemplate;

    public TimerService(GameRepository gameRepository, PlayerRepository playerRepository, RedisTemplate redisTemplate) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.redisTemplate = redisTemplate;
    }

    // 라운드 시작 시 타이머 설정
    public void startNewRound(String roomId) {
        // 방마다 개별적으로 타이머 설정
        int round = gameRepository.updateRound(roomId);
        ScheduledFuture<?> roundTimer = scheduler.schedule(() -> endRound(roomId, round), ROUND_DURATION_SECONDS, TimeUnit.SECONDS);
        roundTimers.put(roomId, roundTimer);

        log.debug("start Timer For {}", roomId);
        // 클라이언트에게 라운드 시작 알림
        notifyClientsOfRoundStart(roomId);
    }

    // 라운드 종료 시 처리 로직
    public void endRound(String roomId, int round) {
        // 라운드가 끝났을 때 필요한 로직 실행 (예: 점수 계산, 상태 업데이트)
        calculateRoundResults(roomId);
        log.debug("end Timer for {}", roomId);
        // WebSocket 서버로 라운드 종료 알림 전송
        notifyClientsOfRoundEnd(roomId);

        // 마지막 라운드라면 종료
        if (round >= FINAL_ROUND) {
            log.debug("게임 종료 호출: {}", roomId);
            endGame(roomId);
        } else {
            startPreparationTimer(roomId);
        }
    }

    // 준비 타이머 로직
    private void startPreparationTimer(String roomId) {
        log.debug("Starting preparation timer for room: {}", roomId);

        // 1분 후에 다음 라운드를 자동으로 시작하도록 타이머 설정
        ScheduledFuture<?> preparationTimer = scheduler.schedule(() -> startNewRound(roomId), PREPARATION_DURATION_SECONDS, TimeUnit.SECONDS);
        roundTimers.put(roomId, preparationTimer);

        notifyClientsOfPreparationStart(roomId);
    }

    private void notifyClientsOfPreparationStart(String roomId) {
        // 클라이언트에게 준비 시간 시작을 알리는 로직
        String targetUrl = socketServerUrl + "/game/preparation-start";
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("roomId", roomId);
        messageData.put("preparationTime", PREPARATION_DURATION_SECONDS); // 준비 시간 1분
        log.debug("SEND TO SOCKET SERVER: {}", targetUrl);
        try {
            restTemplate.postForObject(targetUrl, messageData, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 플레이어의 다음 라운드 시작 준비 여부 확인
    public boolean playerReady(String roomId, String playerId) {
        playerRepository.setPlayerStatus(roomId, playerId, PlayerStatus.READY);
        boolean isAllReady = checkAllPlayersReady(roomId);

        if (isAllReady) {
            cancelPreparationTimer(roomId);
            startNewRound(roomId);
        }
        return isAllReady;
    }

    // 모든 플레이어가 준비되었는지 확인
    public boolean checkAllPlayersReady(String roomId) {
        String playerStatusKey = GAME_PREFIX + roomId + PLAYER_SUFFIX;
        Map<Object, Object> players = redisTemplate.opsForHash().entries(playerStatusKey);
        return players.values().stream().
                allMatch(status -> PlayerStatus.READY.toString().equals(status));
    }

    // 준비 타이머 취소 로직
    private void cancelPreparationTimer(String roomId) {
        ScheduledFuture<?> preparationTimer = roundTimers.get(roomId);
        if (preparationTimer != null && !preparationTimer.isDone()) {
            preparationTimer.cancel(true);
            log.debug("Preparation timer cancelled for room: {}", roomId);
        }
    }

    private void endGame(String roomId) {
        log.debug("게임 종료 로직 실행: {}", roomId);
    }

    private void calculateRoundResults(String roomId) {
        // 각 방의 플레이어 상태 업데이트 로직
        System.out.println("Calculating results for room: " + roomId);
    }


    // 라운드가 시작했음을 알리는 알림 로직
    private void notifyClientsOfRoundStart(String roomId) {
        // WebSocket 서버로 라운드 시작 메시지 전달 (WebSocket 서버가 클라이언트로 전달)
        String targetUrl = socketServerUrl + "/game/round-start";
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("roomId", roomId);
        messageData.put("duration", ROUND_DURATION_SECONDS);
        log.debug("SEND TO SOKET SERVER: {}", targetUrl);
        try {
            restTemplate.postForObject(targetUrl, messageData, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 라운드가 종료했음을 알리는 알림 로직
    private void notifyClientsOfRoundEnd(String roomId) {
        // WebSocket 서버로 라운드 종료 메시지 전달 (WebSocket 서버가 클라이언트로 전달)
        String targetUrl = socketServerUrl + "/game/round-end";
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("roomId", roomId);
        restTemplate.postForObject(targetUrl, messageData, String.class);
    }
}
