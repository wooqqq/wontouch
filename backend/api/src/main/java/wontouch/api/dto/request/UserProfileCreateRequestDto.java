package wontouch.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreateRequestDto {

    private int userId;

    @NotBlank(message = "닉네임은 필수 입력 사항 입니다.")
    @Size(min = 2, max = 6, message = "닉네임은 한글 기준 2자 이상 6자 이하로 설정해주세요.")
    private String nickname;
}
