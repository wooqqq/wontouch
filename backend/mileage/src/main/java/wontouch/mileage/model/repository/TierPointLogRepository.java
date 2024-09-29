package wontouch.mileage.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.mileage.entity.TierPointLog;

@Repository
public interface TierPointLogRepository extends MongoRepository<TierPointLog, Integer> {
}
