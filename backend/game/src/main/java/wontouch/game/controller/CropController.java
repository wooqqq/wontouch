package wontouch.game.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
