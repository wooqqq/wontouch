package wontouch.mileage.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "mileage_log")
public class MileageLog {

    @Id
    private int id;

    private int userId;

    private int amount;

    private String description;

    private int totalMileage;

    private LocalDateTime createAt;
}
