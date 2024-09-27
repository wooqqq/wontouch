package wontouch.api.domain.friend.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import wontouch.api.global.baseTimeEntity.BaseTimeEntity;

@Getter
@Builder
@Document(collection = "friend_request")
public class FriendRequest extends BaseTimeEntity {

    private int fromUserId;

    private int toUserId;
}
