package wontouch.point.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.point.domain.entity.TierPointLog;

@Repository
public interface TierPointLogRepository extends MongoRepository<TierPointLog, Integer> {
}
