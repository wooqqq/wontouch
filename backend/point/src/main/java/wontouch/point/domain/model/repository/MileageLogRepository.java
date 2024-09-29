package wontouch.point.domain.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wontouch.point.domain.entity.MileageLog;

@Repository
public interface MileageLogRepository extends MongoRepository<MileageLog, Integer> {
}
