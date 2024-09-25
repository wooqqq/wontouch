package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wontouch.game.domain.Player;
import wontouch.game.dto.PlayerInitializeDto;
import wontouch.game.service.GameService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/player")
@Slf4j
public class PlayerController {

    private final GameService gameService;

    @Autowired
    public PlayerController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("/init")
    public void initPlayers(@RequestBody List<Player> players) {
        log.debug("players:{}", players.toString());
        gameService.initializePlayers(players);
    }
}
