package wontouch.mileage.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MileageCreateRequestDto {

    @NotNull
    private int userId;

    @NotNull
    private int amount;
}
