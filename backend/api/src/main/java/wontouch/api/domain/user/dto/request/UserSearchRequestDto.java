package wontouch.api.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSearchRequestDto {

    @NotBlank
    private String nickname;
}
