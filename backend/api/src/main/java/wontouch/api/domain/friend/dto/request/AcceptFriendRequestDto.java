package wontouch.api.domain.friend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AcceptFriendRequestDto {

    @NotNull
    private int userId;

    @NotNull
    private int fromUserId;

    @NotNull
    private int toUserId;

}
