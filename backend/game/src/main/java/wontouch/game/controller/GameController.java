package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import wontouch.game.domain.Player;
import wontouch.game.entity.Crop;
import wontouch.game.service.CropService;
import wontouch.game.service.GameService;
import wontouch.game.service.TimerService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {

    @Value("${socket.server.name}:${socket.server.path}")
    private String socketServerUrl;

    private final GameService gameService;
    private final TimerService timerService;
    private final CropService cropService;
    private final RestTemplate restTemplate = new RestTemplate();

    public GameController(GameService gameService, TimerService timerService,
                          CropService cropService) {
        this.gameService = gameService;
        this.timerService = timerService;
        this.cropService = cropService;
    }

    @PostMapping("/init/{roomId}")
    public List<Crop> initGame(@PathVariable String roomId, @RequestBody List<Player> players) {
        log.debug("players:{}", players.toString());
        // 플레이어 정보 세팅
        gameService.initPlayers(roomId, players);
        // 작물 정보 세팅
        List<Crop> cropList = cropService.loadRandomCropsFromEachTypeToRedis(roomId);
        gameService.initPlayersCrops(players, cropList);
        timerService.startNewRound(roomId);

        String targetUrl = socketServerUrl + "/game/crop-list";
        Map<String, Object> cropListResponse = new ConcurrentHashMap<>();
        cropListResponse.put("roomId", roomId);
        cropListResponse.put("cropList", cropList);
        restTemplate.postForObject(targetUrl, cropListResponse, String.class);
        return cropList;
    }
}
