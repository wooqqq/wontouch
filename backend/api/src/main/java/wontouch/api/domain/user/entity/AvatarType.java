package wontouch.api.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AvatarType {
    GOBLIN("goblin", "고블린", 1500),
    KING_GOBLIN("king_goblin", "킹 고블린", 4500),
    GIRL("girl", "여자아이", 100),
    BOY("boy", "남자아이", 100),
    SKELETON("skeleton", "해골",  2500),
    NINJA_SKELETON("ninja_skeleton", "닌자 해골", 5000),
    CURLYHAIR_BOY("curlyhair_boy", "파마머리 남자", 2500),
    FLOWER_GIRL("flower_girl", "꽃을 든 여자", 2500)
    ;

    private String name;
    private String description;
    private int price;

}
