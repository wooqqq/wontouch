package wontouch.game.dto.article;

import wontouch.game.domain.Action;

public class ArticleTransactionRequestDto {
    private String articleId;
    private Action action;
    private long playerId;
}
