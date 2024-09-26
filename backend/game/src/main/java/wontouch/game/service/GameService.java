package wontouch.game.service;

import org.springframework.stereotype.Service;
import wontouch.game.domain.Player;
import wontouch.game.repository.GameRepository;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // 게임이 시작될 때 플레이어들의 초기 상태를 설정하는 메서드
    // TODO 더 많은 필드가 필요한지 모두에게 묻고 추가하기
    public void initPlayers(List<Player> players) {
        for (Player player : players) {
            // 비즈니스 로직 처리
            gameRepository.savePlayer(player);
        }
    }
}
