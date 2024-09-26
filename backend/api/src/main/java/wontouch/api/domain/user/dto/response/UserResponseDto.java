package wontouch.api.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private int id;
    private String username;
    private String email;
    private String nickname;
    private String description;
}
