package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import wontouch.game.dto.shop.CropTransactionRequestDto;
import wontouch.game.dto.shop.CropTransactionResult;
import wontouch.game.entity.Crop;
import wontouch.game.service.CropService;
import wontouch.game.service.ShopService;

import java.util.List;

@RestController
@RequestMapping("/shop")
@Slf4j
public class ShopController {

    private final ShopService shopService;
    private final CropService cropService;

    public ShopController(ShopService shopService, CropService cropService) {
        this.shopService = shopService;
        this.cropService = cropService;
    }

    // 작물 구매
    @PostMapping("/buy/crop/{roomId}")
    public CropTransactionResult buyCrops(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
        CropTransactionResult cropTransactionResult = shopService.buyCrop(roomId, requestDto);
        log.debug("거래 완료 {}", cropTransactionResult);
        return cropTransactionResult;
    }

    // 작물 판매
    @PostMapping("/sell/crop/{roomId}")
    public CropTransactionResult sellCrops(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
        CropTransactionResult cropTransactionResult = shopService.sellCrop(roomId, requestDto);
        log.debug("거래 완료 {}", cropTransactionResult);
        return cropTransactionResult;
    }

    // 마을의 작물 조회
    @GetMapping("/crop/list/{type}")
    public List<Crop> getCrops(@PathVariable String type) {
        return cropService.getCropList(type);
    }
}
