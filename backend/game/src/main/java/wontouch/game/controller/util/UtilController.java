package wontouch.game.controller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wontouch.game.dto.town.CropTransactionRequestDto;
import wontouch.game.dto.town.CropTransactionResult;
import wontouch.game.service.TownService;
import wontouch.game.service.util.UtilService;

// 테스트에만 사용하는 유틸 컨트롤러
@RestController
@RequestMapping("/util")
@Slf4j
public class UtilController {

    private final UtilService utilService;
    private final TownService townService;

    @Autowired
    public UtilController(UtilService utilService, TownService townService) {
        this.utilService = utilService;
        this.townService = townService;
    }

    // 테스트를 위해 해당 플레이어의 보유자산(gold) 10000 증가
    @PostMapping("/show-me-the-money/{playerId}")
    public void showMeTheMoney(@PathVariable String playerId) {
        utilService.showMeTheMoney(playerId);
    }

    @PostMapping("/test-buying/{roomId}")
    public void buyTest(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
        CropTransactionResult cropTransactionResult = townService.buyCrop(roomId, requestDto);
        log.debug("거래 완료 {}", cropTransactionResult);
    }
}
