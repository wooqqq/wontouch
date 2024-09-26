package wontouch.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DescriptionUpdateRequestDto {

    private int userId;

    @Size(max = 15, message = "한줄소개는 한글 기준 15자 이하로 설정해주세요.")
    private String description;
}
