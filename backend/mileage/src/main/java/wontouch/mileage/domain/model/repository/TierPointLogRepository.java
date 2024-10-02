package wontouch.mileage.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wontouch.mileage.domain.entity.TierPointLog;

import java.util.List;
import java.util.Optional;

public interface TierPointLogRepository extends MongoRepository<TierPointLog, Integer> {

    Optional<List<TierPointLog>> findByUserId(int userId);
}
