package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wontouch.game.repository.GameRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class TimerService {
    private static final int ROUND_DURATION_SECONDS = 60;
    private static final int FINAL_ROUND = 5;

    @Value("${socket.server.name}:${socket.server.path}")
    private String socketServerUrl;

    private Map<String, ScheduledFuture<?>> roundTimers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final GameRepository gameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public TimerService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // 라운드 시작 시 타이머 설정
    public void startNewRound(String roomId) {
        // 방마다 개별적으로 타이머 설정
        int round = gameRepository.updateRound(roomId);
        ScheduledFuture<?> roundTimer = scheduler.schedule(() -> endRound(roomId, round), ROUND_DURATION_SECONDS, TimeUnit.SECONDS);
        roundTimers.put(roomId, roundTimer);

        log.debug("start Timer For {}", roomId);
        // 클라이언트에게 라운드 시작 알림
        notifyClientsOfRoundStart(roomId, ROUND_DURATION_SECONDS);
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
            startNewRound(roomId);
        }
    }

    private void endGame(String roomId) {
        log.debug("게임 종료 로직 실행: {}", roomId);
    }

    private void calculateRoundResults(String roomId) {
        // 각 방의 플레이어 상태 업데이트 로직
        System.out.println("Calculating results for room: " + roomId);
    }

    private void notifyClientsOfRoundStart(String roomId, int roundDurationSeconds) {
        // WebSocket 서버로 라운드 시작 메시지 전달 (WebSocket 서버가 클라이언트로 전달)
        String targetUrl = socketServerUrl + "/game/round-start";
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("roomId", roomId);
        messageData.put("duration", roundDurationSeconds);
        log.debug("SEND TO SOKET SERVER: {}", targetUrl);
        try {
            restTemplate.postForObject(targetUrl, messageData, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyClientsOfRoundEnd(String roomId) {
        // WebSocket 서버로 라운드 종료 메시지 전달 (WebSocket 서버가 클라이언트로 전달)
        String targetUrl = socketServerUrl + "/game/round-end";
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("roomId", roomId);
        restTemplate.postForObject(targetUrl, messageData, String.class);
    }
}
