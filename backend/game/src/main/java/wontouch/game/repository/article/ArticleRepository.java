package wontouch.game.repository.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import wontouch.game.repository.crop.CropRedisRepository;

import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class ArticleRepository {

    private final CropRedisRepository cropRepository;

    public ArticleRepository(CropRedisRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    // TODO 라운드 진행 후 기사로 인해 변경된 작물의 가격 계산
    // TODO 각자 메서드의 성격에 맞게 책임 분리
    public void calculateArticleResult(String roomId, int round) {
        Set<Object> allCrops = cropRepository.getAllCrops(roomId);

        List<Object> cropList = allCrops.stream().toList();
        log.debug("cropList: {}", cropList);
        for (Object cropId : cropList) {
            log.debug("cropId:{} 갱신 중 ", cropId);
            int cropPrice = cropRepository.getCropPrice(roomId, cropId);
            log.debug("cropPrice:{}", cropPrice);
            // TODO 기사를 통해 가격 갱신
            int newCropPrice = cropPrice;
            cropRepository.updateCropPrice(roomId, cropId, newCropPrice);
            log.debug("갱신 완료:{}", newCropPrice);
            cropRepository.updateCropChart(roomId, (String) cropId, round, newCropPrice);
            log.debug("차트 업데이트 완료");
        }
    }
}
