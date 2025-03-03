package wontouch.mileage.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MileageCreateRequestDto {

    @NotNull
    private int userId;

    @NotNull
    private int amount;
}
