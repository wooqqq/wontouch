package wontouch.game.repository.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.entity.Article;
import wontouch.game.entity.Crop;
import wontouch.game.repository.crop.CropRedisRepository;
import wontouch.game.repository.crop.CropRepository;

import java.util.*;

@Repository
@Slf4j
public class ArticleRepository {

    private final CropRepository cropRepository;
    private final CropRedisRepository cropRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ROOM_PREFIX = "game:";
    private static final String PLAYER_PREFIX = "player:";
    private static final String CROP_INFIX = ":crop:";
    private static final String CROP_SUFFIX = ":crop";
    private static final String ARTICLE_SUFFIX = ":article";

    public ArticleRepository(CropRepository cropRepository, CropRedisRepository cropRedisRepository, RedisTemplate<String, Object> redisTemplate) {
        this.cropRepository = cropRepository;
        this.cropRedisRepository = cropRedisRepository;
        this.redisTemplate = redisTemplate;
    }

    public Article getArticle(String cropId, String articleId) {
        Optional<Crop> cropByArticleId = cropRepository.findCropByArticleId(cropId, articleId);
        log.debug("cropByArticleId: {}", cropByArticleId);
        if (cropByArticleId.isEmpty() || cropByArticleId.get().getArticleList().isEmpty()) {
            throw new IllegalArgumentException("Article not found for crop: " + cropId);
        }
        Crop crop = cropByArticleId.get();
        Article article = crop.getArticleList().get(0);
        article.setFutureArticles(null);
        return article;
    }

    public Article buyRandomArticle(String roomId, String playerId, String cropId) {
        String articleKey = ROOM_PREFIX + roomId + ARTICLE_SUFFIX + ":" + cropId;
        String playerArticleKey = PLAYER_PREFIX + playerId + ARTICLE_SUFFIX;
        Set<Object> articleIds = redisTemplate.opsForSet().members(articleKey);
        log.debug("crop:{}, articleIds:{}", cropId, articleIds);
        List<Object> articleIdList = new ArrayList<>(Objects.requireNonNull(articleIds));
        Random random = new Random();

        if (!articleIdList.isEmpty()) {
            // 랜덤 인덱스를 통해 랜덤한 값 선택
            Object articleId = articleIdList.get(random.nextInt(articleIdList.size()));
            redisTemplate.opsForSet().add(playerArticleKey, articleId);
            // TODO 처리 로직
            return getArticle(cropId, (String)articleId);
        } else {
            System.out.println("No articles available to buy.");
        }
        return null;
    }

    // TODO 라운드 진행 후 기사로 인해 변경된 작물의 가격 계산
    // TODO 각자 메서드의 성격에 맞게 책임 분리
    public void calculateArticleResult(String roomId, int round) {
        Set<Object> allCrops = cropRedisRepository.getAllCrops(roomId);

        List<Object> cropList = allCrops.stream().toList();
        log.debug("cropList: {}", cropList);
        for (Object cropId : cropList) {
            log.debug("cropId:{} 갱신 중 ", cropId);
            int cropPrice = cropRedisRepository.getCropPrice(roomId, cropId);
            log.debug("cropPrice:{}", cropPrice);
            // TODO 기사를 통해 가격 갱신 (현재는 테스트를 위해 랜덤하게 결정)
            Random random = new Random();
            int randomNumber = random.nextInt(1001);
            int newCropPrice = cropPrice + randomNumber;
            cropRedisRepository.updateCropPrice(roomId, cropId, newCropPrice);
            log.debug("갱신 완료:{}", newCropPrice);
            cropRedisRepository.updateCropChart(roomId, (String) cropId, round, newCropPrice);
            log.debug("차트 업데이트 완료");
        }
    }
}
