package wontouch.api.domain.user.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarDetailResponseDto {

    @NotNull
    private int userId;

    @NotBlank
    private String characterName;

    private boolean isEquipped;

    private boolean isOwned;

    public void updateEquipped(boolean isEquipped) {
        this.isEquipped = isEquipped;
    }

    public void updateOwned(boolean isOwned) {
        this.isOwned = isOwned;
    }
}
