package wontouch.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private int id;
    private String username;
    private String email;
}
