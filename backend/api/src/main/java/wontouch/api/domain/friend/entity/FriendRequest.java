package wontouch.api.domain.friend.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import wontouch.api.global.baseTimeEntity.BaseTimeEntity;

@Data
@Document(collection = "friend_request")
public class FriendRequest extends BaseTimeEntity {

    @Id
    private int id;

    private int fromUserId;

    private int toUserId;
}
