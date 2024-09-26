package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wontouch.game.domain.Player;
import wontouch.game.entity.Crop;
import wontouch.game.repository.GameRepository;
import wontouch.game.repository.player.PlayerRepository;

import java.util.List;

@Service
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    // 게임이 시작될 때 플레이어들의 초기 상태를 설정하는 메서드
    // TODO 더 많은 필드가 필요한지 모두에게 묻고 추가하기
    public void initPlayers(String roomId, List<Player> players) {
        for (Player player : players) {
            // 비즈니스 로직 처리
            playerRepository.savePlayer(roomId, player);
        }
    }

    // 이번 게임에 포함된 작물의 목록을 각 플레이어의 보유 작물에 세팅
    public void initPlayersCrops(List<Player> players, List<Crop> crops) {
        log.debug(crops.toString());
        for (Player player : players) {
            gameRepository.initCrops(player, crops);
        }
    }
}
