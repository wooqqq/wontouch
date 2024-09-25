package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wontouch.game.domain.Player;
import wontouch.game.dto.PlayerInitializeDto;
import wontouch.game.service.GameService;
import wontouch.game.service.TimerService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/player")
@Slf4j
public class PlayerController {

    private final GameService gameService;
    private final TimerService timerService;

    @Autowired
    public PlayerController(GameService gameService, TimerService timerService) {
        this.gameService = gameService;
        this.timerService = timerService;
    }


    @PostMapping("/init/{roomId}")
    public void initPlayers(@PathVariable String roomId, @RequestBody List<Player> players) {
        log.debug("players:{}", players.toString());
        gameService.initializePlayers(players);
        timerService.startNewRound(roomId, 60);
    }
}
