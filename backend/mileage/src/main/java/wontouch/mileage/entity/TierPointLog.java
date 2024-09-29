package wontouch.mileage.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "tier_point_log")
public class TierPointLog {

    @Id
    private int id;

    private int userId;

    private int amount;

    private String description;

    private String tier;

    private LocalDateTime createAt;
}
