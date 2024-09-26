package wontouch.game.controller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wontouch.game.dto.shop.CropTransactionRequestDto;
import wontouch.game.dto.shop.CropTransactionResult;
import wontouch.game.service.ShopService;
import wontouch.game.service.util.UtilService;

// 테스트에만 사용하는 유틸 컨트롤러
@RestController
@RequestMapping("/util")
@Slf4j
public class UtilController {

    private final UtilService utilService;
    private final ShopService shopService;

    @Autowired
    public UtilController(UtilService utilService, ShopService shopService) {
        this.utilService = utilService;
        this.shopService = shopService;
    }

    // 테스트를 위해 해당 플레이어의 보유자산(gold) 10000 증가
    @PostMapping("/show-me-the-money/{playerId}")
    public void showMeTheMoney(@PathVariable String playerId) {
        utilService.showMeTheMoney(playerId);
    }

    @PostMapping("/test-buying/{roomId}")
    public void buyTest(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
        CropTransactionResult cropTransactionResult = shopService.buyCrop(roomId, requestDto);
        log.debug("거래 완료 {}", cropTransactionResult);
    }
}
