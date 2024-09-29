package wontouch.game.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wontouch.game.entity.Article;
import wontouch.game.entity.Crop;
import wontouch.game.service.ArticleService;
import wontouch.game.service.CropService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final CropService cropService;

    public ArticleController(ArticleService articleService, CropService cropService) {
        this.articleService = articleService;
        this.cropService = cropService;
    }

    @PostMapping("/{cropId}")
    public ResponseEntity<Crop> updateArticle(@PathVariable String cropId, @RequestBody List<Article> articleList) {
        log.debug("articleList: {}", articleList);
        Crop crop = articleService.updateArticleList(cropId, articleList);
        log.debug("crop: {}", crop.toString());
        return ResponseEntity.ok(crop);
    }


    @GetMapping("/get-random/{roomId}")
    public ResponseEntity<?> getRandomArticles(@PathVariable String roomId) {
        articleService.saveArticles(roomId, 2);
        return ResponseEntity.ok("random articles");
    }
}
