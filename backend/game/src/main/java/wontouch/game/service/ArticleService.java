package wontouch.game.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wontouch.game.entity.Article;
import wontouch.game.entity.Crop;
import wontouch.game.repository.crop.CropRedisRepository;
import wontouch.game.repository.crop.CropRepository;

import java.util.*;

@Service
@Slf4j
public class ArticleService {

    private final CropRepository cropRepository;
    private final CropRedisRepository cropRedisRepository;

    public ArticleService(CropRepository cropRepository, CropRedisRepository cropRedisRepository) {
        this.cropRepository = cropRepository;
        this.cropRedisRepository = cropRedisRepository;
    }

    // 작물의 기사 업데이트
    public Crop updateArticleList(String cropId, List<Article> articleList) {
        Optional<Crop> findCrop = cropRepository.findById(cropId);
        Crop crop = findCropOrThrow(findCrop);
        List<Article> currentArticleList = crop.getArticleList();
        if (currentArticleList == null) {
            // 기존 Article 리스트가 없으면 새로운 리스트로 설정
            crop.setArticleList(articleList);
        } else {
            // 기존 Article 리스트가 있으면 새로 받은 리스트를 추가
            currentArticleList.addAll(articleList);
        }

        return cropRepository.save(crop);
    }

    // 랜덤한 기사 리스트 로드
    public List<Article> loadRandomArticleList(String roomId, int numArticles) {
        Set<Object> allCrops = cropRedisRepository.getAllCrops(roomId);
        log.debug("allCrops from loadRandomArticleList: {}", allCrops);
        List<Article> randomArticles = new ArrayList<>();
        Random random = new Random();

        for (Object cropId : allCrops) {
            Optional<Crop> findCrop = cropRepository.findCropWithoutFutureArticles((String) cropId);
            Crop crop = findCropOrThrow(findCrop);
            List<Article> articleList = crop.getArticleList();
            if (articleList != null && !articleList.isEmpty()) {
                // 랜덤하게 지정한 수의 기사를 뽑아냄
                List<Article> randomSelection = getRandomArticles(articleList, numArticles, random);
                randomArticles.addAll(randomSelection);
            }
        }
        return randomArticles;
    }

    // 랜덤한 기사 리스트를 가져오기
    private List<Article> getRandomArticles(List<Article> articleList, int numArticles, Random random) {
        List<Article> selectedArticles = new ArrayList<>();
        int articleSize = articleList.size();

        // 뽑고자 하는 기사 수가 실제 기사 수보다 많을 경우 모두 반환
        if (numArticles > articleSize) {
            return new ArrayList<>(articleList);
        }

        // 중복 없이 랜덤하게 기사 선택
        while (selectedArticles.size() < numArticles) {
            int randomIndex = random.nextInt(articleSize);
            Article selectedArticle = articleList.get(randomIndex);

            // 중복된 기사가 선택되지 않도록 처리
            if (!selectedArticles.contains(selectedArticle)) {
                selectedArticles.add(selectedArticle);
            }
        }
        return selectedArticles;
    }

    // 찾은 작물 예외 처리
    private Crop findCropOrThrow(Optional<Crop> findCrop) {
        if (findCrop.isEmpty()) {
            throw new IllegalArgumentException("crop not found");
        }
        return findCrop.get();
    }
}
