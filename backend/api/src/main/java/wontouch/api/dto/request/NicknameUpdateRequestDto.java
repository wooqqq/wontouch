package wontouch.api.dto.request;

import lombok.Getter;

@Getter
public class NicknameUpdateRequestDto {

    private int userId;
    private String nickname;
}
