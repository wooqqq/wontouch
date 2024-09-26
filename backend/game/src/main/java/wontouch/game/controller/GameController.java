package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import wontouch.game.domain.Player;
import wontouch.game.service.CropService;
import wontouch.game.service.GameService;
import wontouch.game.service.TimerService;

import java.util.List;

@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {

    private final GameService gameService;
    private final TimerService timerService;
    private final CropService cropService;

    public GameController(GameService gameService, TimerService timerService,
                          CropService cropService) {
        this.gameService = gameService;
        this.timerService = timerService;
        this.cropService = cropService;
    }

    @PostMapping("/init/{roomId}")
    public void initGame(@PathVariable String roomId, @RequestBody List<Player> players) {
        log.debug("players:{}", players.toString());
        // 플레이어 정보 세팅
        gameService.initPlayers(players);
        // 작물 정보 세팅
        cropService.loadRandomCropsFromEachTypeToRedis(roomId);
        timerService.startNewRound(roomId);
    }
}
