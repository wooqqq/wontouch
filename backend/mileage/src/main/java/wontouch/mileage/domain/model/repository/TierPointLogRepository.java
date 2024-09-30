package wontouch.mileage.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.mileage.domain.entity.TierPointLog;

@Repository
public interface TierPointLogRepository extends MongoRepository<TierPointLog, Integer> {
}
