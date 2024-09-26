package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wontouch.game.service.GameService;
import wontouch.game.service.PlayerService;
import wontouch.game.service.TimerService;

import java.util.Map;

@RestController
@RequestMapping("/player")
@Slf4j
public class PlayerController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final TimerService timerService;

    @Autowired
    public PlayerController(GameService gameService, PlayerService playerService, TimerService timerService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.timerService = timerService;
    }


//    @PostMapping("/init/{roomId}")
//    public void initPlayers(@PathVariable String roomId, @RequestBody List<Player> players) {
//        log.debug("players:{}", players.toString());
//        gameService.initPlayers(roomId, players);
//        timerService.startNewRound(roomId);
//    }

    @GetMapping("/crop/list/{playerId}")
    public Map<Object, Object> getCropList(@PathVariable int playerId) {
        return playerService.getAllCropsByPlayer(playerId);
    }
}
