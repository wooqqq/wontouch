package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import wontouch.game.dto.shop.CropTransactionRequestDto;
import wontouch.game.dto.shop.CropTransactionResult;
import wontouch.game.service.ShopService;

@RestController
@RequestMapping("/shop")
@Slf4j
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/buy/crop/{roomId}")
    public CropTransactionResult buyCrops(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
        CropTransactionResult cropTransactionResult = shopService.buyCrop(roomId, requestDto);
        log.debug("거래 완료 {}", cropTransactionResult);
        return cropTransactionResult;
    }
}
