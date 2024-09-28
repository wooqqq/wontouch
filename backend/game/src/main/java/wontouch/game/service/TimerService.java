package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.game.domain.PlayerStatus;
import wontouch.game.repository.GameRepository;
import wontouch.game.repository.article.ArticleRepository;
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
    private final Map<String, ScheduledFuture<?>> roundTimers = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ArticleRepository articleRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate<String, Object> redisTemplate;

    public TimerService(GameRepository gameRepository, PlayerRepository playerRepository, ArticleRepository articleRepository, RedisTemplate<String, Object> redisTemplate) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.articleRepository = articleRepository;
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
        log.debug("end Timer for {}", roomId);
        // WebSocket 서버로 라운드 종료 알림 전송
        notifyClientsOfRoundEnd(roomId);

        // TODO 작물 가격 계산 -> Redis에 반영 -> 라운드 결과 출력
        articleRepository.calculateArticleResult(roomId, round);
        log.debug("작물 가격 계산 완료 후 반영: {}", roomId);
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

        // TODO 작물 가격 기록
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
    public Map<String, Object> playerReady(String roomId, String playerId) {
        playerRepository.setPlayerStatus(roomId, playerId, PlayerStatus.READY);
        Map<String, Object> readyInfo = checkAllPlayersReady(roomId);
        boolean allReady = (boolean) readyInfo.get("allReady");
        if (allReady) {
            cancelPreparationTimer(roomId);
            startNewRound(roomId);
        }
        return readyInfo;
    }

    // 모든 플레이어가 준비되었는지 확인
    public Map<String, Object> checkAllPlayersReady(String roomId) {
        String playerStatusKey = GAME_PREFIX + roomId + PLAYER_SUFFIX;
        Map<Object, Object> players = redisTemplate.opsForHash().entries(playerStatusKey);

        // 총 플레이어 수
        int totalPlayers = players.size();

        // 준비된 플레이어 수
        long readyPlayers = players.values().stream()
                .filter(status -> PlayerStatus.READY.toString().equals(status))
                .count();

        // 모두 준비되었는지 여부
        boolean allReady = (totalPlayers == readyPlayers);

        // 로그로 출력
        log.debug("Total players: {}, Ready players: {}, All ready: {}", totalPlayers, readyPlayers, allReady);

        // 결과를 맵에 담아서 반환 (원하면 boolean 대신 세부 정보도 반환 가능)
        Map<String, Object> result = new HashMap<>();
        result.put("totalPlayers", totalPlayers);
        result.put("readyPlayers", readyPlayers);
        result.put("allReady", allReady);

        return result;
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
        Map<String, Integer> totalGold = gameRepository.getTotalGold(roomId);
        log.debug("최종 결과 테이블 출력: {}", totalGold);
        // TODO 잠시 다른거 하는 중
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
