package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wontouch.game.entity.Crop;
import wontouch.game.repository.crop.CropRedisRepository;
import wontouch.game.repository.crop.CropRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    // 작물 저장
    public Crop saveCrop(Crop crop) {
        return cropRepository.save(crop);
    }

    // 작물 전체 조회
    public Map<Object, Object> getTownCropList(String roomId, String type) {
        Map<Object, Object> cropList = new ConcurrentHashMap<>();
        List<Object> cropIdsFromTown = cropRedisRepository.getCropIdsFromTown(roomId, type).stream().toList();
        for (Object cropId : cropIdsFromTown) {
            Map<String, Integer> cropInfo = new ConcurrentHashMap<>();
            int cropQuantity = cropRedisRepository.getCropQuantity(roomId, cropId);
            int cropPrice = cropRedisRepository.getCropPrice(roomId, cropId);
            cropInfo.put("cropQuantity", cropQuantity);
            cropInfo.put("cropPrice", cropPrice);
            cropList.put(cropId, cropInfo);
        }
        return cropList;
    }

    // 게임 시작 시 mongoDB에서 타입마다 NUM_OF_CROPS_PER_TYPE 만큼의 작물을 뽑아 Redis에 저장
    public List<Crop> loadRandomCropsFromEachTypeToRedis(String roomId) {
        List<String> allTypes = cropRepository.findDistinctTypes();
        System.out.println(allTypes);
        // 이번 게임에서 사용되는 모든 작물 리스트
        List<Crop> gameCropList = new ArrayList<>();
        for (String type : allTypes) {
            List<Crop> selectedRandomCrops = selectRandomCrops(type);
            gameCropList.addAll(selectedRandomCrops);
            // TODO roomId와 Mapping
            initCropsInfoToRedis(roomId, type, selectedRandomCrops);
            log.debug("saved " + type + " crops");
        }
        return gameCropList;
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
            cropRedisRepository.addCropToTown(roomId, type, crop.getId());
            cropRedisRepository.addCropDetails(roomId, crop);
        }
    }
}
