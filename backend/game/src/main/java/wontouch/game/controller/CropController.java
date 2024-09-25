package wontouch.game.controller;

import org.springframework.web.bind.annotation.*;
import wontouch.game.entity.Crop;
import wontouch.game.service.CropService;

@RestController
@RequestMapping("/crop")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }


    @PostMapping
    public Crop addCrop(@RequestBody Crop crop) {
        return cropService.saveCrop(crop);
    }

    // 게임 시작 전 타입마다 crop을 뽑아 redis에 저장할 예정
//    @PostMapping("/load")
//    public void loadCrop() {
//        cropService.loadRandomCropsFromEachTypeToRedis();
//    }
}
