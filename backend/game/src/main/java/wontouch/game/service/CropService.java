package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wontouch.game.entity.Crop;
import wontouch.game.repository.crop.CropRedisRepository;
import wontouch.game.repository.crop.CropRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class CropService {

    // TODO redis 저장 시 crop의 ID를 사용할지 crop의 Name을 사용할지 결정

    private static final int NUM_OF_CROPS_PER_TYPE = 3;
    private final CropRepository cropRepository;
    private final CropRedisRepository cropRedisRepository;

    @Autowired
    public CropService(CropRepository cropRepository, CropRedisRepository cropRedisRepository) {
        this.cropRepository = cropRepository;
        this.cropRedisRepository = cropRedisRepository;
    }

    // crop 저장
    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }

    // 게임 시작 시 mongoDB에서 타입마다 NUM_OF_CROPS_PER_TYPE 만큼의 작물을 뽑아 Redis에 저장
    public void loadRandomCropsFromEachTypeToRedis(String roomId) {
        List<String> allTypes = cropRepository.findDistinctTypes();
        System.out.println(allTypes);

        for (String type : allTypes) {
            List<Crop> selectedRandomCrops = selectRandomCrops(type);
            // TODO roomId와 Mapping
            initCropsInfoToRedis(roomId, type, selectedRandomCrops);
            log.debug("saved " + type + " crops");
        }
    }

    // 타입에 해당하는 작물 중 랜덤하게 선택
    private List<Crop> selectRandomCrops(String type) {
        List<Crop> crops = cropRepository.findByType(type);
        int numOfCrops = NUM_OF_CROPS_PER_TYPE;
        if (numOfCrops > crops.size()) {
            numOfCrops = crops.size();
        }
        Random random = new Random();
        return crops.stream()
                .sorted((a, b) -> random.nextInt(2) - 1)  // 무작위로 정렬
                .limit(numOfCrops)                // 제한된 개수 선택
                .toList();
    }

    // 해당하는 작물을 Redis에 저장
    private void initCropsInfoToRedis(String roomId, String type, List<Crop> selectedCrops) {
        for (Crop crop : selectedCrops) {
            cropRedisRepository.addCropToShop(roomId, type, crop.getId());
            cropRedisRepository.addCropDetails(roomId, crop);
        }
    }
}
