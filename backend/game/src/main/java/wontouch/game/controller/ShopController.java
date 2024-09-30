package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import wontouch.game.dto.town.CropTransactionRequestDto;
import wontouch.game.dto.town.CropTransactionResult;

@Controller
@RequestMapping("/shop")
@Slf4j
public class ShopController {

//    // 작물 구매
//    @PostMapping("/buy/article/{roomId}")
//    public CropTransactionResult buyCrops(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
//        CropTransactionResult cropTransactionResult = townService.buyCrop(roomId, requestDto);
//        log.debug("거래 완료 {}", cropTransactionResult);
//        return cropTransactionResult;
//    }
//
//    // 작물 판매
//    @PostMapping("/sell/article/{roomId}")
//    public CropTransactionResult sellCrops(@PathVariable String roomId, @RequestBody CropTransactionRequestDto requestDto) {
//        CropTransactionResult cropTransactionResult = townService.sellCrop(roomId, requestDto);
//        log.debug("거래 완료 {}", cropTransactionResult);
//        return cropTransactionResult;
//    }
}
