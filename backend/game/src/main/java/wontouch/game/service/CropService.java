package wontouch.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wontouch.game.entity.Crop;
import wontouch.game.repository.CropRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    private final CropRepository cropRepository;

    @Autowired
    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    public Optional<Crop> getCropByName(String cropName) {
        return cropRepository.findByName(cropName);
    }

    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }
}
