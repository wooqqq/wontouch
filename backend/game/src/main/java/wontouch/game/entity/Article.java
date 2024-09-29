package wontouch.game.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Article {
    private String crop;
    private String aspect;
    private String title;
    private String body;
    private String author; ;

    @JsonProperty("future_articles")
    private List<FutureArticle> futureArticles;
}
