package wontouch.api.domain.friend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveFriendRequestDto {

    private int fromUserId;
    private String fromUserNickname;
    private String fromUserTier;
    private String fromUserCharacterName;
}
