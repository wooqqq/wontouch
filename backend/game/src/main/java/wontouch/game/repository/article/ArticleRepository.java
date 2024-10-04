package wontouch.game.repository.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import wontouch.game.dto.article.ArticleTransactionResult;
import wontouch.game.dto.TransactionStatusType;
import wontouch.game.entity.Article;
import wontouch.game.entity.Crop;
import wontouch.game.entity.FutureArticle;
import wontouch.game.entity.SubCrop;
import wontouch.game.repository.crop.CropRedisRepository;
import wontouch.game.repository.crop.CropRepository;

import java.util.*;

import static wontouch.game.domain.RedisKeys.*;

@Repository
@Slf4j
public class ArticleRepository {

    private final CropRepository cropRepository;
    private final CropRedisRepository cropRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int ARTICLE_PRICE_INCREMENT = 200;

    public ArticleRepository(CropRepository cropRepository, CropRedisRepository cropRedisRepository, RedisTemplate<String, Object> redisTemplate) {
        this.cropRepository = cropRepository;
        this.cropRedisRepository = cropRedisRepository;
        this.redisTemplate = redisTemplate;
    }

    // 기사 내용 획득
    public Article getArticle(String cropId, String articleId) {
        Optional<Crop> cropByArticleId = cropRepository.findCropByArticleId(cropId, articleId);
        log.debug("cropByArticleId: {}", cropByArticleId);
        if (cropByArticleId.isEmpty() || cropByArticleId.get().getArticleList().isEmpty()) {
            throw new IllegalArgumentException("Article not found for crop: " + cropId);
        }
        Crop crop = cropByArticleId.get();
        Article article = crop.getArticleList().get(0);
        return article;
    }

    // 랜덤한 기사 구매
    public ArticleTransactionResult buyRandomArticle(String roomId, String playerId, String cropId) {
        String articleKey = GAME_PREFIX + roomId + ARTICLE_SUFFIX + ":" + cropId;
        String playerArticleKey = PLAYER_PREFIX + playerId + ARTICLE_SUFFIX;
        String playerInfoKey = PLAYER_PREFIX + playerId + INFO_SUFFIX;

        int articlePrice = (Integer) redisTemplate.opsForHash().get(playerInfoKey, "articlePrice");
        Integer gold = (Integer) redisTemplate.opsForHash().get(playerInfoKey, "gold");
        if (gold == null || gold < articlePrice) {
            return new ArticleTransactionResult(TransactionStatusType.INSUFFICIENT_GOLDS, null);
        }

        Set<Object> articleIds = redisTemplate.opsForSet().members(articleKey);
        log.debug("crop:{}, articleIds:{}", cropId, articleIds);
        List<Object> articleIdList = new ArrayList<>(Objects.requireNonNull(articleIds));
        Random random = new Random();


        if (!articleIdList.isEmpty()) {
            // 랜덤 인덱스를 통해 랜덤한 값 선택
            Object articleId = articleIdList.get(random.nextInt(articleIdList.size()));
            log.debug("articlePrice: {}", articlePrice);
            // 골드 차감
            redisTemplate.opsForHash().increment(playerInfoKey, "gold", -articlePrice);
            log.debug("article payed");
            redisTemplate.opsForHash().increment(playerInfoKey, "articlePrice", ARTICLE_PRICE_INCREMENT);
            log.debug("article Price is increased");
            redisTemplate.opsForSet().add(playerArticleKey, articleId);
            log.debug("add article");
            // TODO 처리 로직
            try {
                Article article = getArticle(cropId, (String) articleId);
                article.setFutureArticles(null);
                return new ArticleTransactionResult(TransactionStatusType.SUCCESS, article);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No articles available to buy.");
        }
        return null;
    }

    // TODO 라운드 진행 후 기사로 인해 변경된 작물의 가격 계산
    // TODO 각자 메서드의 성격에 맞게 책임 분리
    public synchronized Map<String, Integer> calculateArticleResult(String roomId, int round) {
        Set<Object> allCrops = cropRedisRepository.getAllCrops(roomId);

        List<Object> cropList = allCrops.stream().toList();
        log.debug("cropList: {}", cropList);

        Map<String, Integer> priceMap = initializePriceMap(roomId, allCrops);
        Map<String, Integer> newPriceMap = calculateNewPrices(roomId, allCrops, priceMap);

        // 가격 갱신 및 차트 업데이트
        for (Object cropId : allCrops) {
            updateCropPrice(roomId, (String) cropId, newPriceMap.get(cropId), round);
        }

        return newPriceMap;
    }

    // 현재 가격 맵
    private Map<String, Integer> initializePriceMap(String roomId, Set<Object> allCrops) {
        Map<String, Integer> priceMap = new HashMap<>();
        for (Object cropId : allCrops) {
            int cropPrice = cropRedisRepository.getCropPrice(roomId, cropId);
            priceMap.put((String) cropId, cropPrice);
        }
        return priceMap;
    }

    // 새로운 가격 맵 반환
    private Map<String, Integer> calculateNewPrices(String roomId, Set<Object> allCrops, Map<String, Integer> priceMap) {
        Map<String, Integer> newPriceMap = new HashMap<>(priceMap);
        for (Object cropId : allCrops) {
            // 작물에 해당하는 기사ID 반환
            Set<Object> articleIds = getAllArticleIds(roomId, (String) cropId);
            for (Object articleId : articleIds) {
                try {
                    Article article = getArticle((String) cropId, (String) articleId); // 기사 ID를 통해 실제 객체 반환
                    List<FutureArticle> futureArticles = article.getFutureArticles(); // 미래 결과 리스트를 확인
                    FutureArticle futureArticle = selectFutureArticle(futureArticles); //확률을 통해 기사 선택
                    log.debug("futureArticle: {}", futureArticle); // 선택된 결과 확인
                    if (futureArticle != null) {
                        double changeRate = futureArticle.getChangeRate();
                        double updatedPrice = newPriceMap.get(cropId) + (priceMap.get(cropId) * changeRate / 100);
                        log.debug("crop:{}, updatedPrice: {}", cropId, updatedPrice);
                        newPriceMap.put((String) cropId, (int) updatedPrice);

                        // SubCrops 처리
                        List<SubCrop> subCrops = futureArticle.getSubCrops();
                        for (SubCrop subCrop : subCrops) {
                            String subCropId = subCrop.getId();
                            double subChangeRate = subCrop.getChangeRate();
                            double subUpdatedPrice = newPriceMap.get(subCropId) + (priceMap.get(subCropId) * subChangeRate / 100);
                            newPriceMap.put((String) subCropId, (int) subUpdatedPrice);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return newPriceMap;
    }

    public FutureArticle selectFutureArticle(List<FutureArticle> futureArticles) {
        Random random = new Random();
        double randomValue = random.nextDouble();  // 0과 1 사이의 랜덤 값 생성
        double positiveRate = futureArticles.get(0).getSpawnRate() / 100;
        double negativeRate = futureArticles.get(1).getSpawnRate() / 100;
        double naturalRate = futureArticles.get(2).getSpawnRate() / 100;

        // 세 비율의 합을 구함
        double totalRate = positiveRate + negativeRate + naturalRate;

        // 0.99가 되도록 각 비율을 재조정
        positiveRate = positiveRate / totalRate * 0.99;
        negativeRate = negativeRate / totalRate * 0.99;
        naturalRate = naturalRate / totalRate * 0.99;
        log.debug("RANDOM VALUE: {}, positiveRate: {}, negativeRate: {}, naturalRate: {}", randomValue, positiveRate, negativeRate, naturalRate);
        if (randomValue < positiveRate) {
            log.debug("POSITIVE EVENT");
            return futureArticles.get(0);
        } else if (randomValue < positiveRate + negativeRate) {
            log.debug("NEGATIVE EVENT");
            return futureArticles.get(1);
        } else if (randomValue < positiveRate + negativeRate + naturalRate) {
            log.debug("NATURAL EVENT");
            return futureArticles.get(2);
        } else {
            System.out.println("SPECIAL EVENT");
            return null;
        }
    }

    public void updateCropPrice(String roomId, String cropId, int newPrice, int round) {
        cropRedisRepository.updateCropPrice(roomId, cropId, newPrice);
        cropRedisRepository.updateCropChart(roomId, cropId, round, newPrice);
        log.debug("Crop price updated for cropId: {} with new price: {}", cropId, newPrice);
    }

    public Set<Object> getAllArticleIds(String roomId, String cropId) {
        String articleKey = GAME_PREFIX + roomId + ARTICLE_SUFFIX + ":" + cropId;
        return redisTemplate.opsForSet().members(articleKey);
    }
}
