package wontouch.api.domain.notification.dto.request;

import lombok.Getter;

@Getter
public class GameInviteRequestDto {

    private long senderId;
    private long receiverId;
    private String roomId;
    private String roomName;
    private String password;
}
